package com.on.server.domain.board.presentation;

import com.on.server.domain.board.application.BoardService;
import com.on.server.domain.board.domain.BoardType;
import com.on.server.domain.post.dto.PostResponseDTO;
import com.on.server.domain.user.domain.User;
import com.on.server.global.common.CommonResponse;
import com.on.server.global.security.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "게시글 작성")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class BoardController {

    private final BoardService boardService;
    private final SecurityService securityService;

    // 특정 게시판(boardType)의 모든 게시글을 조회
    @Operation(summary = "특정 게시판의 모든 게시글 조회")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    @GetMapping("/{boardType}")
    public ResponseEntity<List<PostResponseDTO>> getAllPostsByBoardType(
            @PathVariable("boardType") BoardType boardType
    ) {
        List<PostResponseDTO> posts = boardService.getAllPostsByBoardType(boardType);

        return ResponseEntity.ok(posts);
    }

    // 자기가 특정 게시판에 작성한 모든 게시글 조회
    @Operation(summary = "사용자가 특정 게시판에 작성한 모든 게시글 조회")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    @GetMapping("/user/{boardType}")
    public ResponseEntity<List<PostResponseDTO>> getPostsByUserIdAndBoardType(
            @PathVariable("boardType") BoardType boardType,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = securityService.getUserByUserDetails(userDetails);
        List<PostResponseDTO> posts = boardService.getPostsByUserIdAndBoardType(user, boardType);

        return ResponseEntity.ok(posts);
    }

    // 특정 게시판의 최신 게시글 4개 조회
    @Operation(summary = "특정 게시판의 최신 게시글 4개 조회")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    @GetMapping("/recent/{boardType}")
    public ResponseEntity<List<PostResponseDTO>> getLatestPosts(
            @PathVariable("boardType") BoardType boardType
    ) {
        List<PostResponseDTO> latestPosts = boardService.getLatestPostsByBoardType(boardType);
        return ResponseEntity.ok(latestPosts);
    }
}
