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
import org.springframework.beans.factory.annotation.Autowired;
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


    // 1. 특정 게시글의 모든 댓글 조회
    @Transactional(readOnly = true)
    public List<CommentResponseDTO> getAllCommentsByPostId(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        List<Comment> comments = commentRepository.findByPost(post);
        return comments.stream()
                .map(this::mapToCommentResponseDTO)
                .collect(Collectors.toList());
    }

    // 2. 새로운 댓글 작성
    public CommentResponseDTO createComment(CommentRequestDTO commentRequestDTO) {
        User user = userRepository.findById(commentRequestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Post post = postRepository.findById(commentRequestDTO.getPostId())
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

    // Comment 엔티티를 CommentResponseDTO로 매핑하는 메서드
    private CommentResponseDTO mapToCommentResponseDTO(Comment comment) {
        return CommentResponseDTO.builder()
                .commentId(comment.getId())
                .userId(comment.getUser().getId())
                .postId(comment.getPost().getId())
                .isAnonymous(comment.getIsAnonymous())
                .contents(comment.getContents())
                .build();
    }

}

