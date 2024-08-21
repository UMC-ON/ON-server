package com.on.server.domain.comment.application;

import com.on.server.domain.alarm.application.AlertService;
import com.on.server.domain.alarm.application.FcmService;
import com.on.server.domain.alarm.domain.AlertType;
import com.on.server.domain.alarm.dto.FcmRequestDto;
import com.on.server.domain.board.domain.BoardType;
import com.on.server.domain.comment.domain.Comment;
import com.on.server.domain.comment.domain.repository.CommentRepository;
import com.on.server.domain.comment.dto.CommentRequestDTO;
import com.on.server.domain.comment.dto.CommentResponseDTO;
import com.on.server.domain.post.domain.Post;
import com.on.server.domain.post.domain.repository.PostRepository;
import com.on.server.domain.user.domain.User;
import com.on.server.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FcmService fcmService;
    private final AlertService alertService;

    // 특정 게시글의 모든 댓글 및 답글 조회
    @Transactional(readOnly = true)
    public List<CommentResponseDTO> getAllCommentsAndRepliesByPostId(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        List<Comment> comments = commentRepository.findByPost(post);
        return comments.stream()
                .map(this::mapToCommentResponseDTO)
                .collect(Collectors.toList());
    }

    // 특정 댓글의 모든 답글 조회
    @Transactional(readOnly = true)
    public List<CommentResponseDTO> getAllRepliesByCommentId(Long commentId) {
        Comment parentComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        List<Comment> replies = commentRepository.findByParentComment(parentComment); // 수정된 부분
        return replies.stream()
                .map(this::mapToCommentResponseDTO)
                .collect(Collectors.toList());
    }


    // 새로운 댓글 작성
    public CommentResponseDTO createComment(Long postId, CommentRequestDTO commentRequestDTO) throws IOException {
        User user = userRepository.findById(commentRequestDTO.getId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        Integer anonymousIndex = null;
        if (commentRequestDTO.isAnonymous()) {
            anonymousIndex = generateAnonymousIndex(user, post);
        }

        Comment comment = Comment.builder()
                .contents(commentRequestDTO.getContents())
                .isAnonymous(commentRequestDTO.isAnonymous())
                .post(post)
                .user(user)
                .anonymousIndex(anonymousIndex)
                .build();

        commentRepository.save(comment);

        String title;
        AlertType alertType;
        if(post.getBoard().getType() == BoardType.INFO) { //정보게시판 댓글
            title = "내 정보글에 새로운 댓글이 달렸어요.";
            alertType = AlertType.정보;
        }
        else { //자유게시판 댓글
            title = "내 자유글에 새로운 댓글이 달렸어요.";
            alertType = AlertType.자유;
        }
        String body = commentRequestDTO.getContents();

        fcmService.sendMessage(post.getUser().getDeviceToken(), alertType, title, body);

        FcmRequestDto fcmRequestDto = FcmRequestDto.builder()
                .title(title)
                .body(body)
                .alertType(alertType)
                .alertConnectId(post.getId())
                .build();

        alertService.saveAlert(post.getUser(), fcmRequestDto);

        return mapToCommentResponseDTO(comment);
    }

    // 답글 작성
    public CommentResponseDTO createReply(Long parentCommentId, CommentRequestDTO commentRequestDTO) throws IOException {
        User user = userRepository.findById(commentRequestDTO.getId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new RuntimeException("부모 댓글을 찾을 수 없습니다."));

        Integer anonymousIndex = null;
        if (commentRequestDTO.isAnonymous()) {
            anonymousIndex = generateAnonymousIndex(user, parentComment.getPost());
        }

        Comment reply = Comment.builder()
                .contents(commentRequestDTO.getContents())
                .isAnonymous(commentRequestDTO.isAnonymous())
                .post(parentComment.getPost())
                .user(user)
                .parentComment(parentComment)
                .anonymousIndex(anonymousIndex)
                .build();

        commentRepository.save(reply);

        Post post = postRepository.findById(parentComment.getPost().getId())
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        String title;
        AlertType alertType;
        if(post.getBoard().getType() == BoardType.INFO) { //정보게시판 댓글
            title = "내 정보글에 새로운 대댓글이 달렸어요.";
            alertType = AlertType.정보;
        }
        else { //자유게시판 댓글
            title = "내 자유글에 새로운 대댓글이 달렸어요.";
            alertType = AlertType.자유;
        }
        String body = commentRequestDTO.getContents();

        fcmService.sendMessage(post.getUser().getDeviceToken(), alertType, title, body);

        FcmRequestDto fcmRequestDto = FcmRequestDto.builder()
                .title(title)
                .body(body)
                .alertType(alertType)
                .alertConnectId(post.getId())
                .build();

        alertService.saveAlert(post.getUser(), fcmRequestDto);
        return mapToCommentResponseDTO(reply);
    }

    // 익명 인덱스 생성 로직
    private Integer generateAnonymousIndex(User user, Post post) {
        List<Comment> userComments = commentRepository.findByUserAndPostAndIsAnonymousTrue(user, post);
        if (!userComments.isEmpty()) {
            return userComments.get(0).getAnonymousIndex();
        }

        List<Integer> existingIndices = commentRepository.findAnonymousIndicesByPost(post);
        int newIndex = existingIndices.isEmpty() ? 1 : existingIndices.stream().max(Integer::compare).orElse(0) + 1;
        return newIndex;
    }

    // Comment 엔티티를 CommentResponseDTO로 매핑하는 메서드
    private CommentResponseDTO mapToCommentResponseDTO(Comment comment) {
        boolean isReply = comment.getParentComment() != null;

        Long replyId = null;

        if (isReply) {
            // 부모 댓글의 자식 댓글들 중 현재 댓글의 인덱스를 찾아서 replyId로 설정
            List<Comment> siblings = comment.getParentComment().getChildrenComment();
            replyId = (long) (siblings.indexOf(comment) + 1);
        }

        String nickname = comment.getIsAnonymous() ? "익명" + comment.getAnonymousIndex() : comment.getUser().getNickname();

        CommentResponseDTO.WriterInfo writerInfo = CommentResponseDTO.WriterInfo.builder()
                .id(comment.getUser().getId())
                .nickname(nickname)
                .build();

        return CommentResponseDTO.builder()
                .commentId(isReply ? comment.getParentComment().getId() : comment.getId())
                .replyId(replyId)
                .postId(comment.getPost().getId())
                .writerInfo(writerInfo)
                .isAnonymous(comment.getIsAnonymous())
                .contents(comment.getContents())
                .replyCount(comment.getChildrenComment() != null ? comment.getChildrenComment().size() : 0)
                .build();
    }

}
