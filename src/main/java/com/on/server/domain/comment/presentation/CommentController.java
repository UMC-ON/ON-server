package com.on.server.domain.comment.presentation;

import com.on.server.domain.comment.application.CommentService;
import com.on.server.domain.comment.dto.CommentRequestDTO;
import com.on.server.domain.comment.dto.CommentResponseDTO;
import com.on.server.domain.user.domain.User;
import com.on.server.global.security.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/api/v1/comment")
public class CommentController {

    private final CommentService commentService;
    private final SecurityService securityService;

    // 1. 특정 게시글(postId)의 모든 댓글 및 답글 조회
    @Operation(summary = "특정 게시글의 모든 댓글 및 답글 조회")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    @GetMapping("/{postId}")
    public ResponseEntity<Page<CommentResponseDTO>> getAllCommentsAndRepliesByPostId(
            @PathVariable("postId") Long postId,
            Pageable pageable
    ) {
        Page<CommentResponseDTO> comments = commentService.getAllCommentsAndRepliesByPostId(postId, pageable);
        return ResponseEntity.ok(comments);
    }

    // 2. 특정 댓글(commentId)의 모든 답글 조회
    @Operation(summary = "특정 댓글의 모든 답글 조회")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    @GetMapping("/{commentId}/reply")
    public ResponseEntity<Page<CommentResponseDTO>> getAllRepliesByCommentId(
            @PathVariable("commentId") Long commentId,
            Pageable pageable
    ) {
        Page<CommentResponseDTO> replies = commentService.getAllRepliesByCommentId(commentId, pageable);
        return ResponseEntity.ok(replies);
    }

    // 3. 특정 게시글(postId)에 새로운 댓글을 작성
    @Operation(summary = "특정 게시글에 댓글 작성")
    @PostMapping("/{postId}")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    public ResponseEntity<CommentResponseDTO> createComment(
            @PathVariable("postId") Long postId,
            @RequestBody CommentRequestDTO commentRequestDTO,
            @AuthenticationPrincipal UserDetails userDetails)
    {
        User user = securityService.getUserByUserDetails(userDetails);

        CommentResponseDTO createdComment = commentService.createComment(postId, commentRequestDTO, user);
        return ResponseEntity.ok(createdComment);
    }

    // 4. 특정 댓글(CommentId)에 답글 작성
    @Operation(summary = "특정 댓글에 답글 작성")
    @PostMapping("/{commentId}/reply")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    public ResponseEntity<CommentResponseDTO> createReply(
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentRequestDTO commentRequestDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = securityService.getUserByUserDetails(userDetails);

        CommentResponseDTO createdReply = commentService.createReply(commentId, commentRequestDTO, user);
        return ResponseEntity.ok(createdReply);
    }
}


