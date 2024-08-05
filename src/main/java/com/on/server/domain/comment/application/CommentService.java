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
        return null;
    }

    // 2. 새로운 댓글 작성
    public CommentResponseDTO createComment(CommentRequestDTO commentRequestDTO) {
        return null;
    }

}

