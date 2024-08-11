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
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    // 1. 특정 게시판(boardType)의 모든 게시글을 조회
    @Operation(summary = "특정 게시판의 모든 게시글 조회")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
    @GetMapping("/{boardType}")
    public ResponseEntity<List<PostResponseDTO>> getAllPostsByBoardType(@PathVariable BoardType boardType, @AuthenticationPrincipal UserDetails userDetails) {
        // PostService를 호출하여 특정 boardType에 해당하는 게시글 목록을 가져옴
        List<PostResponseDTO> posts = postService.getAllPostsByBoardType(boardType);

        // 조회된 게시글 목록을 응답 본문에 담아 HTTP 200 OK 상태와 함께 반환
        return ResponseEntity.ok(posts);
    }

    // 2. 특정 게시판(boardType)에 새로운 게시글을 작성
    @Operation(summary = "특정 게시판에 새로운 게시글 작성")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE')")
    @PostMapping("/{boardType}")
    public ResponseEntity<PostResponseDTO> createPost(@PathVariable BoardType boardType, @RequestBody PostRequestDTO postRequestDTO, @AuthenticationPrincipal UserDetails userDetails) {
        // PostService를 호출하여 새로운 게시글을 생성
        PostResponseDTO createdPost = postService.createPost(boardType, postRequestDTO);

        // 생성된 게시글을 응답 본문에 담아 HTTP 201 Created 상태와 함께 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    // 3. 특정 게시판(boardType) 내의 특정 게시글(postId)을 조회
    @Operation(summary = "특정 게시판 내의 특정 게시글 조회")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
    @GetMapping("/{boardType}/{postId}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable BoardType boardType, @PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails) {
        // PostService를 호출하여 특정 boardType과 postId에 해당하는 게시글을 가져옴
        PostResponseDTO post = postService.getPostById(boardType, postId);

        // 조회된 게시글을 응답 본문에 담아 HTTP 200 OK 상태와 함께 반환
        return ResponseEntity.ok(post);
    }

    // 4. 자기가 특정 게시판에 작성한 모든 게시글 조회
    @Operation(summary = "사용자가 특정 게시판에 작성한 모든 게시글 조회")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
    @GetMapping("/user/{userId}/{boardType}")
    public ResponseEntity<List<PostResponseDTO>> getPostsByUserIdAndBoardType(@PathVariable Long userId, @PathVariable BoardType boardType, @AuthenticationPrincipal UserDetails userDetails) {
        List<PostResponseDTO> posts = postService.getPostsByUserIdAndBoardType(userId, boardType);
        return ResponseEntity.ok(posts);
    }

    // 5. 자기가 특정 게시판에 작성한 특정 게시글(postId)을 삭제
    @Operation(summary = "사용자가 특정 게시판에 작성한 특정 게시글 삭제")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE')")
    @DeleteMapping("/user/{userId}/{boardType}/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long userId, @PathVariable BoardType boardType, @PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails) {
        // PostService를 호출하여 특정 boardType, postId 및 userId에 해당하는 게시글을 삭제
        postService.deletePost(userId, boardType, postId);

        // HTTP 204 No Content 상태를 반환하여 성공적으로 삭제되었음을 알림
        return ResponseEntity.noContent().build();
    }
}
