package com.on.server.domain.scrap.presentation;

import com.on.server.domain.scrap.application.ScrapService;
import com.on.server.domain.scrap.dto.ScrapRequestDTO;
import com.on.server.domain.scrap.dto.ScrapResponseDTO;
import com.on.server.domain.user.domain.User;
import com.on.server.global.security.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "물품거래글 스크랩")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/scrap")
public class ScrapController {

    private final ScrapService scrapService;
    private final SecurityService securityService;

    // 1. 특정 물품글 스크랩
    @Operation(summary = "특정 물품거래글 스크랩")
    @PostMapping
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    public ResponseEntity<Void> scrapMarketPost(@RequestBody ScrapRequestDTO scrapRequestDTO, @AuthenticationPrincipal UserDetails userDetails) {

        User user = securityService.getUserByUserDetails(userDetails);

        scrapService.scrapMarketPost(user, scrapRequestDTO);
        return ResponseEntity.ok().build();
    }

    // 2. 스크랩한 모든 물품글 조회
    @Operation(summary = "스크랩한 모든 물품거래글 조회")
    @GetMapping
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    public ResponseEntity<List<ScrapResponseDTO>> getScrappedMarketPosts(@AuthenticationPrincipal UserDetails userDetails) {

        User user = securityService.getUserByUserDetails(userDetails);

        List<ScrapResponseDTO> posts = scrapService.getScrappedMarketPosts(user);
        return ResponseEntity.ok(posts);
    }

    // 3. 스크랩 목록에서 특정 물품글 스크랩 취소 (삭제)
    @Operation(summary = "스크랩 목록에서 특정 물품글 스크랩 취소")
    @DeleteMapping("/{marketPostId}")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    public ResponseEntity<Void> deleteScrap(@PathVariable Long marketPostId, @AuthenticationPrincipal UserDetails userDetails) {

        User user = securityService.getUserByUserDetails(userDetails);

        scrapService.deleteMarketPost(user, marketPostId);
        return ResponseEntity.ok().build();
    }
}