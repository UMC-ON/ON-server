package com.on.server.domain.post.presentation;

import com.on.server.domain.post.application.PostService;
import com.on.server.domain.post.dto.PostRequestDTO;
import com.on.server.domain.post.dto.PostResponseDTO;
import com.on.server.domain.board.domain.BoardType;
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

@Tag(name = "게시글 작성")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {

    private final PostService postService;

    // 1. 특정 게시판(boardType)의 모든 게시글을 조회
    @Operation(summary = "특정 게시판의 모든 게시글 조회")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
    @GetMapping("/{boardType}")
    public ResponseEntity<List<PostResponseDTO>> getAllPostsByBoardType(@PathVariable("boardType") BoardType boardType, @AuthenticationPrincipal UserDetails userDetails) {
        List<PostResponseDTO> posts = postService.getAllPostsByBoardType(boardType);

        return ResponseEntity.ok(posts);
    }

    // 2. 특정 게시판(boardType)에 새로운 게시글을 작성
    @Operation(summary = "특정 게시판에 새로운 게시글 작성")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE')")
    @PostMapping("/{boardType}")
    public ResponseEntity<PostResponseDTO> createPost(@PathVariable("boardType") BoardType boardType, @RequestBody PostRequestDTO postRequestDTO, @AuthenticationPrincipal UserDetails userDetails) {
        PostResponseDTO createdPost = postService.createPost(boardType, postRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    // 3. 특정 게시판(boardType) 내의 특정 게시글(postId)을 조회
    @Operation(summary = "특정 게시판 내의 특정 게시글 조회")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
    @GetMapping("/{boardType}/{postId}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable("boardType") BoardType boardType, @PathVariable("postId") Long postId, @AuthenticationPrincipal UserDetails userDetails) {
        PostResponseDTO post = postService.getPostById(boardType, postId);

        return ResponseEntity.ok(post);
    }

    // 4. 자기가 특정 게시판에 작성한 모든 게시글 조회
    @Operation(summary = "사용자가 특정 게시판에 작성한 모든 게시글 조회")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
    @GetMapping("/user/{userId}/{boardType}")
    public ResponseEntity<List<PostResponseDTO>> getPostsByUserIdAndBoardType(@PathVariable("userId") Long userId, @PathVariable("boardType") BoardType boardType, @AuthenticationPrincipal UserDetails userDetails) {
        List<PostResponseDTO> posts = postService.getPostsByUserIdAndBoardType(userId, boardType);

        return ResponseEntity.ok(posts);
    }

    // 5. 자기가 특정 게시판에 작성한 특정 게시글(postId)을 삭제
    @Operation(summary = "사용자가 특정 게시판에 작성한 특정 게시글 삭제")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE')")
    @DeleteMapping("/user/{userId}/{boardType}/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable("userId") Long userId, @PathVariable("boardType") BoardType boardType,@PathVariable("postId") Long postId, @AuthenticationPrincipal UserDetails userDetails) {
        postService.deletePost(userId, boardType, postId);

        return ResponseEntity.noContent().build();
    }
}
