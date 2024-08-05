package com.on.server.domain.comment.presentation;

import com.on.server.domain.comment.application.CommentService;
import com.on.server.domain.comment.dto.CommentRequestDTO;
import com.on.server.domain.comment.dto.CommentResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 1. 특정 게시글(postId)의 모든 댓글을 조회
    @GetMapping
    public ResponseEntity<List<CommentResponseDTO>> getAllCommentsByPostId(@PathVariable Long postId) {

        List<CommentResponseDTO> comments = commentService.getAllCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    // 2. 특정 게시글(postId)에 새로운 댓글을 작성
    @PostMapping
    public ResponseEntity<CommentResponseDTO> createComment(@PathVariable Long postId, @RequestBody CommentRequestDTO commentRequestDTO) {

        commentRequestDTO.setPostId(postId); // RequestDTO에 postId를 설정
        CommentResponseDTO createdComment = commentService.createComment(commentRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

}

