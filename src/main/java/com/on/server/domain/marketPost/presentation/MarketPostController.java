package com.on.server.domain.marketPost.presentation;

import com.on.server.domain.marketPost.application.MarketPostService;
import com.on.server.domain.marketPost.domain.DealStatus;
import com.on.server.domain.marketPost.domain.DealType;
import com.on.server.domain.marketPost.dto.MarketPostRequestDTO;
import com.on.server.domain.marketPost.dto.MarketPostResponseDTO;
import com.on.server.domain.scrap.application.ScrapService;
import com.on.server.domain.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "물품거래글 작성")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/market-post")
public class MarketPostController {

    private final MarketPostService marketPostService;
    private final ScrapService scrapService;

    // 1. 모든 물품글 조회
    @Operation(summary = "모든 물품거래글 조회")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
    @GetMapping
    public ResponseEntity<List<MarketPostResponseDTO>> getAllMarketPosts(@AuthenticationPrincipal UserDetails userDetails) {
        List<MarketPostResponseDTO> posts = marketPostService.getAllMarketPosts();
        return ResponseEntity.ok(posts);
    }

    // 2. 특정 물품글 조회
    @Operation(summary = "특정 물품거래글 조회")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
    @GetMapping("/{marketPostId}")
    public ResponseEntity<MarketPostResponseDTO> getMarketPostById(@PathVariable Long marketPostId, @AuthenticationPrincipal UserDetails userDetails) {
        MarketPostResponseDTO post = marketPostService.getMarketPostById(marketPostId);
        return ResponseEntity.ok(post);
    }

    // 3. 새로운 물품글 작성
    @Operation(summary = "새로운 물품거래글 작성")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MarketPostResponseDTO> createMarketPost(@ModelAttribute MarketPostRequestDTO requestDTO, @AuthenticationPrincipal UserDetails userDetails) {
        // 현재 인증된 사용자의 ID를 DTO에 설정
        if (userDetails instanceof User) {
            User user = (User) userDetails;
            requestDTO.setUserId(user.getId());
        }

        MarketPostResponseDTO createdPost = marketPostService.createMarketPost(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    // 4. 마이페이지에서 자기가 작성한 모든 물품글 조회
    @Operation(summary = "자기가 작성한 모든 물품거래글 조회")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MarketPostResponseDTO>> getMarketPostsByUserId(@PathVariable Long userId, @AuthenticationPrincipal UserDetails userDetails) {
        List<MarketPostResponseDTO> posts = marketPostService.getMarketPostsByUserId(userId);
        return ResponseEntity.ok(posts);
    }

    // 5. 마이페이지에서 자기가 작성한 특정 게시글 삭제
    @Operation(summary = "자기가 작성한 특정 동행구하기 게시글 삭제")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE')")
    @DeleteMapping("/user/{userId}/{marketPostId}")
    public ResponseEntity<Void> deleteMarketPost(@PathVariable Long userId, @PathVariable Long marketPostId, @AuthenticationPrincipal UserDetails userDetails) {
        marketPostService.deleteMarketPost(userId, marketPostId);
        return ResponseEntity.noContent().build();
    }

    // 6. 필터링된 물품글 조회 (국가와 거래 유형에 따라 필터링)
    @Operation(summary = "필터링된 물품거래글 조회")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
    @GetMapping("/filter")
    public ResponseEntity<List<MarketPostResponseDTO>> getFilteredMarketPosts(
            @RequestParam(required = false) DealType dealType,
            @RequestParam(required = false) String currentCountry,
            @AuthenticationPrincipal UserDetails userDetails) {

        List<MarketPostResponseDTO> filteredPosts = marketPostService.getFilteredMarketPosts(dealType, currentCountry);
        return ResponseEntity.ok(filteredPosts);
    }

    // 7. 거래가능 물품만 보기 (DealStatus가 AWAIT인 것만 조회)
    @Operation(summary = "거래 가능 물품만 보기")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
    @GetMapping("/available")
    public ResponseEntity<List<MarketPostResponseDTO>> getAvailableMarketPosts(@AuthenticationPrincipal UserDetails userDetails) {

        // DealStatus를 AWAIT으로 고정하여 필터링
        List<MarketPostResponseDTO> availablePosts = marketPostService.getAvailableMarketPosts();
        return ResponseEntity.ok(availablePosts);
    }

    // 8. 거래 상태 업데이트 (예: 거래완료로 변경)
    @Operation(summary = "거래 상태 업데이트")
    @PutMapping("/{marketPostId}/status")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE')")
    public ResponseEntity<MarketPostResponseDTO> updateMarketPostStatus(
            @PathVariable Long marketPostId,
            @RequestParam DealStatus status,
            @AuthenticationPrincipal UserDetails userDetails) {

        MarketPostResponseDTO updatedPost = marketPostService.updateMarketPostStatus(marketPostId, status);
        return ResponseEntity.ok(updatedPost);
    }
}