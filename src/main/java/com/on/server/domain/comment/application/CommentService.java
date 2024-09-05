package com.on.server.domain.comment.application;

import com.on.server.domain.comment.domain.Comment;
import com.on.server.domain.comment.domain.repository.CommentRepository;
import com.on.server.domain.comment.dto.CommentRequestDTO;
import com.on.server.domain.comment.dto.CommentResponseDTO;
import com.on.server.domain.comment.dto.WriterInfo;
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
    private final PostRepository postRepository;

    // 특정 게시글의 모든 댓글 및 답글 조회
    @Transactional(readOnly = true)
    public List<CommentResponseDTO> getAllCommentsAndRepliesByPostId(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        List<Comment> comments = commentRepository.findByPost(post);
        return comments.stream()
                .map(CommentResponseDTO::from)
                .collect(Collectors.toList());
    }

    // 특정 댓글의 모든 답글 조회
    @Transactional(readOnly = true)
    public List<CommentResponseDTO> getAllRepliesByCommentId(Long commentId) {
        Comment parentComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        List<Comment> replies = commentRepository.findByParentComment(parentComment);
        return replies.stream()
                .map(CommentResponseDTO::from)
                .collect(Collectors.toList());
    }


    // 새로운 댓글 작성
    public CommentResponseDTO createComment(Long postId, CommentRequestDTO commentRequestDTO, User user) {
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
        return CommentResponseDTO.from(comment);
    }

    // 답글 작성
    public CommentResponseDTO createReply(Long parentCommentId, CommentRequestDTO commentRequestDTO, User user) {
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
        return CommentResponseDTO.from(reply);
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
