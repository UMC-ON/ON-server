package com.on.server.domain.post.presentation;

import com.on.server.domain.board.domain.Board;
import com.on.server.domain.post.application.PostService;
import com.on.server.domain.post.domain.Post;
import com.on.server.domain.post.dto.PostRequestDTO;
import com.on.server.domain.post.dto.PostResponseDTO;
import com.on.server.domain.board.domain.BoardType;
import com.on.server.domain.user.domain.User;
import com.on.server.global.security.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "게시글 작성")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {

    private final PostService postService;
    private final SecurityService securityService;

    // 특정 게시판(boardType)에 새로운 게시글을 작성
    @Operation(summary = "특정 게시판에 새로운 게시글 작성")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    @PostMapping(value = "/{boardType}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponseDTO> createPost(
            @PathVariable("boardType") BoardType boardType,
            @RequestPart("postRequestDTO") PostRequestDTO requestDTO,
            @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
            @AuthenticationPrincipal UserDetails userDetails)
    {
        User user = securityService.getUserByUserDetails(userDetails);

        PostResponseDTO createdPost = postService.createPost(boardType, requestDTO, imageFiles, user);
        return ResponseEntity.ok(createdPost);
    }

    // 특정 게시판(boardType) 내의 특정 게시글(postId)을 조회
    @Operation(summary = "특정 게시판 내의 특정 게시글 조회")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    @GetMapping("/{boardType}/{postId}")
    public ResponseEntity<PostResponseDTO> getPostById(
            @PathVariable("boardType") BoardType boardType,
            @PathVariable("postId") Long postId
    ) {
        PostResponseDTO post = postService.getPostById(boardType, postId);
        return ResponseEntity.ok(post);
    }


    // 자기가 특정 게시판에 작성한 특정 게시글(postId)을 삭제
    @Operation(summary = "사용자가 특정 게시판에 작성한 특정 게시글 삭제")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    @DeleteMapping("/user/{boardType}/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable("boardType") BoardType boardType,
            @PathVariable("postId") Long postId,
            @AuthenticationPrincipal UserDetails userDetails)
    {
        User user = securityService.getUserByUserDetails(userDetails);

        postService.deletePost(user, boardType, postId);
        return ResponseEntity.ok().build();
    }


    // 국가 필터링 API
    @Operation(summary = "국가 필터링된 게시글 조회")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    @GetMapping("/filter/{boardType}")
    public ResponseEntity<List<PostResponseDTO>> searchPostsByCountry(
            @PathVariable("boardType") BoardType boardType,
            @RequestParam(name = "country") String country
    ) {
        List<PostResponseDTO> filteredPosts = postService.getPostsByCountryAndBoardType(boardType, country);
        return ResponseEntity.ok(filteredPosts);
    }


    // 게시글 검색 API
    @Operation(summary = "게시글 검색")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    @GetMapping("/search")
    public ResponseEntity<Page<PostResponseDTO>>searchPosts(
            @RequestParam("keyword") String keyword,
            @ParameterObject Pageable pageable
    ) {
        Page<PostResponseDTO> posts = postService.searchPosts(keyword, pageable);
        return ResponseEntity.ok(posts);
    }
}
