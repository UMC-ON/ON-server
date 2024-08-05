package com.on.server.domain.post.presentation;

import com.on.server.domain.post.application.PostService;
import com.on.server.domain.post.dto.PostRequestDTO;
import com.on.server.domain.post.dto.PostResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "게시글 작성")
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    // 1. 특정 게시판(boardId)의 모든 게시글을 조회
    @Operation(summary = "특정 게시판의 모든 게시글 조회")
    @GetMapping("/{boardId}")
    public ResponseEntity<List<PostResponseDTO>> getAllPostsByBoardId(@PathVariable Long boardId) {

        // PostService를 호출하여 특정 boardId에 해당하는 게시글 목록을 가져옴
        List<PostResponseDTO> posts = postService.getAllPostsByBoardId(boardId);

        // 조회된 게시글 목록을 응답 본문에 담아 HTTP 200 OK 상태와 함께 반환
        return ResponseEntity.ok(posts);
    }

    // 2. 특정 게시판(boardId)에 새로운 게시글을 작성
    @PostMapping("/{boardId}")
    @Operation(summary = "특정 게시판에 새로운 게시글 작성")
    public ResponseEntity<PostResponseDTO> createPost(@PathVariable Long boardId, @RequestBody PostRequestDTO postRequestDTO) {

        // PostService를 호출하여 새로운 게시글을 생성
        PostResponseDTO createdPost = postService.createPost(boardId, postRequestDTO);

        // 생성된 게시글을 응답 본문에 담아 HTTP 201 Created 상태와 함께 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    // 3. 특정 게시판(boardId) 내의 특정 게시글(postId)을 조회
    @GetMapping("/{boardId}/{postId}")
    @Operation(summary = "특정 게시판 내의 특정 게시글 조회")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long boardId, @PathVariable Long postId) {

        // PostService를 호출하여 특정 boardId와 postId에 해당하는 게시글을 가져옴
        PostResponseDTO post = postService.getPostById(boardId, postId);

        // 조회된 게시글을 응답 본문에 담아 HTTP 200 OK 상태와 함께 반환
        return ResponseEntity.ok(post);
    }

    // 4. 자기가 특정 게시판에 작성한 모든 게시글 조회
    @GetMapping("/user/{userId}/{boardId}")
    @Operation(summary = "사용자가 특정 게시판에 작성한 모든 게시글 조회")
    public ResponseEntity<List<PostResponseDTO>> getPostsByUserIdAndBoardId(@PathVariable Long userId, @PathVariable Long boardId) {

        List<PostResponseDTO> posts = postService.getPostsByUserIdAndBoardId(userId, boardId);
        return ResponseEntity.ok(posts);
    }

    // 4. 자기가 특정 게시판에 작성한 특정 게시글(postId)을 삭제
    @DeleteMapping("/user/{userId}/{boardId}/{postId}")
    @Operation(summary = "사용자가 특정 게시판에 작성한 특정 게시글 삭제")
    public ResponseEntity<Void> deletePost(@PathVariable Long userId, @PathVariable Long boardId, @PathVariable Long postId) {

        // PostService를 호출하여 특정 boardId, postId 및 userId에 해당하는 게시글을 삭제
        postService.deletePost(userId, boardId, postId);

        // HTTP 204 No Content 상태를 반환하여 성공적으로 삭제되었음을 알림
        return ResponseEntity.noContent().build();
    }

}
