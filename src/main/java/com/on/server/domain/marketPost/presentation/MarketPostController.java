package com.on.server.domain.marketPost.presentation;

import com.on.server.domain.marketPost.application.MarketPostService;
import com.on.server.domain.marketPost.domain.DealStatus;
import com.on.server.domain.marketPost.domain.DealType;
import com.on.server.domain.marketPost.dto.MarketPostRequestDTO;
import com.on.server.domain.marketPost.dto.MarketPostResponseDTO;
import com.on.server.domain.user.domain.User;
import com.on.server.global.security.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "물품거래글 작성")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/market-post")
public class MarketPostController {

    private final MarketPostService marketPostService;
    private final SecurityService securityService;

    // 1. 모든 물품글 조회
    @Operation(summary = "모든 물품거래글 조회")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    @GetMapping
    public ResponseEntity<Page<MarketPostResponseDTO>> getAllMarketPosts(@ParameterObject Pageable pageable) {
        Page<MarketPostResponseDTO> posts = marketPostService.getAllMarketPosts(pageable);
        return ResponseEntity.ok(posts);
    }

    // 2. 특정 물품글 조회
    @Operation(summary = "특정 물품거래글 조회")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    @GetMapping("/{marketPostId}")
    public ResponseEntity<MarketPostResponseDTO> getMarketPostById(@PathVariable Long marketPostId) {
        MarketPostResponseDTO post = marketPostService.getMarketPostById(marketPostId);
        return ResponseEntity.ok(post);
    }

    // 3. 새로운 물품글 작성
    @Operation(summary = "새로운 물품거래글 작성")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MarketPostResponseDTO> createMarketPost(@RequestPart("requestDTO") MarketPostRequestDTO requestDTO,
                                                                  @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
                                                                  @AuthenticationPrincipal UserDetails userDetails) {

        User user = securityService.getUserByUserDetails(userDetails);

        MarketPostResponseDTO createdPost = marketPostService.createMarketPost(user, requestDTO, imageFiles);
        return ResponseEntity.ok(createdPost);
    }

    // 4. 마이페이지에서 자기가 작성한 모든 물품글 조회
    @Operation(summary = "자기가 작성한 모든 물품거래글 조회")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    @GetMapping("/user")
    public ResponseEntity<Page<MarketPostResponseDTO>> getMarketPostsByUser(@AuthenticationPrincipal UserDetails userDetails,
                                                                            @ParameterObject Pageable pageable) {
        User user = securityService.getUserByUserDetails(userDetails);

        Page<MarketPostResponseDTO> posts = marketPostService.getMarketPostsByUser(user, pageable);
        return ResponseEntity.ok(posts);
    }

    // 5. 마이페이지에서 자기가 작성한 특정 게시글 삭제
    @Operation(summary = "자기가 작성한 물품거래 게시글 삭제")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    @DeleteMapping("/user/{marketPostId}")
    public ResponseEntity<Void> deleteMarketPost(@PathVariable Long marketPostId,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        User user = securityService.getUserByUserDetails(userDetails);

        marketPostService.deleteMarketPost(user, marketPostId);
        return ResponseEntity.ok().build();
    }

    // 6. 필터링된 물품글 조회 (국가와 거래 유형에 따라 필터링)
    @Operation(summary = "필터링된 물품거래글 조회")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    @GetMapping("/filter")
    public ResponseEntity<Page<MarketPostResponseDTO>> getFilteredMarketPosts(
            @RequestParam(required = false) DealType dealType,
            @RequestParam(required = false) String currentCountry,
            @RequestParam(required = false) DealStatus dealStatus,
            @ParameterObject Pageable pageable) {

        Page<MarketPostResponseDTO> filteredPosts = marketPostService.getFilteredMarketPosts(dealType, currentCountry, dealStatus, pageable);
        return ResponseEntity.ok(filteredPosts);
    }

    // 7. 거래가능 물품만 보기 (DealStatus가 AWAIT인 것만 조회)
    @Operation(summary = "거래 가능 물품만 보기")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    @GetMapping("/available")
    public ResponseEntity<Page<MarketPostResponseDTO>> getAvailableMarketPosts(@ParameterObject Pageable pageable) {

        // DealStatus를 AWAIT으로 고정하여 필터링
        Page<MarketPostResponseDTO> availablePosts = marketPostService.getAvailableMarketPosts(pageable);
        return ResponseEntity.ok(availablePosts);
    }

    // 8. 거래 상태 업데이트
    @Operation(summary = "거래 상태 업데이트")
    @PutMapping("/{marketPostId}/status")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    public ResponseEntity<MarketPostResponseDTO> updateMarketPostStatus(@PathVariable Long marketPostId) throws IOException {

        MarketPostResponseDTO updatedPost = marketPostService.updateMarketPostStatus(marketPostId);
        return ResponseEntity.ok(updatedPost);
    }

    // 9. 검색 기능
    @Operation(summary = "검색")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    @GetMapping("/search")
    public ResponseEntity<Page<MarketPostResponseDTO>> searchMarketPosts(@RequestParam String keyword,
                                                                         @ParameterObject Pageable pageable) {
        Page<MarketPostResponseDTO> searchResults = marketPostService.searchMarketPosts(keyword, pageable);
        return ResponseEntity.ok(searchResults);
    }

    // 내 주변 물품거래글
    // 특정 게시글의 위치를 기준으로 근처 게시글 조회
    @Operation(summary = "내 주변 물품거래글")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    @GetMapping("/{marketPostId}/nearby")
    public ResponseEntity<List<MarketPostResponseDTO>> getNearbyMarketPosts(@PathVariable Long marketPostId) {
        MarketPostResponseDTO post = marketPostService.getMarketPostById(marketPostId);
        List<MarketPostResponseDTO> nearbyPosts = marketPostService.getNearbyMarketPosts(post.getCurrentCountry(), marketPostId);
        return ResponseEntity.ok(nearbyPosts);
    }
}