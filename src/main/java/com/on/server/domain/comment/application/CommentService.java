package com.on.server.domain.comment.application;

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

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

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
    public CommentResponseDTO createComment(Long postId, CommentRequestDTO commentRequestDTO) {
        User user = userRepository.findById(commentRequestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        Comment comment = Comment.builder()
                .contents(commentRequestDTO.getContents())
                .isAnonymous(commentRequestDTO.isAnonymous())
                .post(post)
                .user(user)
                .build();

        commentRepository.save(comment);
        return mapToCommentResponseDTO(comment);
    }

    // 답글 작성
    public CommentResponseDTO createReply(Long parentCommentId, CommentRequestDTO commentRequestDTO) {
        User user = userRepository.findById(commentRequestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new RuntimeException("부모 댓글을 찾을 수 없습니다."));

        Comment reply = Comment.builder()
                .contents(commentRequestDTO.getContents())
                .isAnonymous(commentRequestDTO.isAnonymous())
                .post(parentComment.getPost())
                .user(user)
                .parentComment(parentComment)
                .build();

        commentRepository.save(reply);
        return mapToCommentResponseDTO(reply);
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

        return CommentResponseDTO.builder()
                .commentId(isReply ? comment.getParentComment().getId() : comment.getId()) // 최상위 댓글 또는 부모 댓글 ID
                .replyId(replyId) // 답글인 경우 순차적인 replyId
                .userId(comment.getUser().getId())
                .postId(comment.getPost().getId())
                .isAnonymous(comment.getIsAnonymous())
                .userNickname(comment.getUser().getNickname())
                .contents(comment.getContents())
                .replyCount(comment.getChildrenComment() != null ? comment.getChildrenComment().size() : 0) // 답글 수 계산
                .build();
    }

}
