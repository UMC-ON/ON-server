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
import com.on.server.global.common.exceptions.BadRequestException;
import com.on.server.global.common.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final FcmService fcmService;
    private final AlertService alertService;

    // 특정 게시글의 댓글을 페이징으로 조회하고, 댓글에 대한 모든 답글을 반환
    @Transactional(readOnly = true)
    public Page<CommentResponseDTO> getAllCommentsAndRepliesByPostId(Long postId, Pageable pageable) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BadRequestException(ResponseCode.ROW_DOES_NOT_EXIST, "게시글을 찾을 수 없습니다. ID: " + postId));

        Page<Comment> parentComments  = commentRepository.findByPost(post, pageable);

        List<CommentResponseDTO> responseDTOs = parentComments.stream()
                .flatMap(parentComment -> {
                    List<CommentResponseDTO> result = new ArrayList<>();
                    result.add(CommentResponseDTO.from(parentComment, commentRepository));

                    List<Comment> replies = commentRepository.findAllByParentComment(parentComment);
                    result.addAll(replies.stream()
                            .map(reply -> CommentResponseDTO.from(reply, commentRepository))
                            .collect(Collectors.toList()));

                    return result.stream();
                })
                .collect(Collectors.toList());

        return new PageImpl<>(responseDTOs, pageable, parentComments.getTotalElements());
    }

    // 특정 댓글의 모든 답글 조회
    @Transactional(readOnly = true)
    public Page<CommentResponseDTO> getAllRepliesByCommentId(Long commentId, Pageable pageable) {
        Comment parentComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BadRequestException(ResponseCode.ROW_DOES_NOT_EXIST, "댓글을 찾을 수 없습니다. ID: " + commentId));

        Page<Comment> replies = commentRepository.findByParentComment(parentComment, pageable);
        return replies.map(reply -> CommentResponseDTO.from(reply, commentRepository));
    }


    // 새로운 댓글 작성
    public CommentResponseDTO createComment(Long postId, CommentRequestDTO commentRequestDTO, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BadRequestException(ResponseCode.ROW_DOES_NOT_EXIST, "게시글을 찾을 수 없습니다. ID: " + postId));

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
            alertType = AlertType.INFORMATION;
        }
        else { //자유게시판 댓글
            title = "내 자유글에 새로운 댓글이 달렸어요.";
            alertType = AlertType.FREE;
        }
        String body = commentRequestDTO.getContents();

        alertService.sendAndSaveAlert(post.getUser(), alertType, title, body, post.getId());

        return CommentResponseDTO.from(comment, commentRepository);
    }

    // 답글 작성
    public CommentResponseDTO createReply(Long parentCommentId, CommentRequestDTO commentRequestDTO, User user) {
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new BadRequestException(ResponseCode.ROW_DOES_NOT_EXIST, "부모 댓글을 찾을 수 없습니다. ID: " + parentCommentId));

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

        AlertType alertType;
        if(post.getBoard().getType() == BoardType.INFO) { //정보게시판 댓글
            alertType = AlertType.INFORMATION;
        }
        else { //자유게시판 댓글
            alertType = AlertType.FREE;
        }
        String title = "새로운 대댓글이 달렸어요.";
        String body = commentRequestDTO.getContents();

        alertService.sendAndSaveAlert(post.getUser(), alertType, title, body, post.getId());

        return CommentResponseDTO.from(reply, commentRepository);
    }

    // 익명 인덱스 생성
    private Integer generateAnonymousIndex(User user, Post post) {
        List<Comment> userComments = commentRepository.findByUserAndPostAndIsAnonymousTrue(user, post);
        if (!userComments.isEmpty()) {
            return userComments.get(0).getAnonymousIndex();
        }

        List<Integer> existingIndices = commentRepository.findAnonymousIndicesByPost(post);
        int newIndex = existingIndices.isEmpty() ? 1 : existingIndices.stream().max(Integer::compare).orElse(0) + 1;
        return newIndex;
    }

}
