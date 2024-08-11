package com.on.server.domain.comment.presentation;

import com.on.server.domain.comment.application.CommentService;
import com.on.server.domain.comment.dto.CommentRequestDTO;
import com.on.server.domain.comment.dto.CommentResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "댓글 작성")
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    // 1. 특정 게시글(postId)의 모든 댓글을 조회
    @Operation(summary = "특정 게시글의 모든 댓글 조회")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentResponseDTO>> getAllCommentsByPostId(@PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails) {

        List<CommentResponseDTO> comments = commentService.getAllCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }


    // 2. 특정 게시글(postId)에 새로운 댓글을 작성
    @Operation(summary = "특정 게시글에 댓글 작성")
    @PostMapping("/{postId}")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE')")
    public ResponseEntity<CommentResponseDTO> createComment(@PathVariable Long postId,
                                                            @RequestBody CommentRequestDTO commentRequestDTO,
                                                            @AuthenticationPrincipal UserDetails userDetails) {

        CommentResponseDTO createdComment = commentService.createComment(postId, commentRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

}

