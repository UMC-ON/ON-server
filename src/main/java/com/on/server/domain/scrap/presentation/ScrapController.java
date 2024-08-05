package com.on.server.domain.scrap.presentation;

import com.on.server.domain.scrap.application.ScrapService;
import com.on.server.domain.scrap.dto.ScrapRequestDTO;
import com.on.server.domain.scrap.dto.ScrapResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scraps")
public class ScrapController {

    private final ScrapService scrapService;

    @Autowired
    public ScrapController(ScrapService scrapService) {
        this.scrapService = scrapService;
    }

    // 1. 특정 물품글 스크랩
    @PostMapping
    public ResponseEntity<Void> scrapMarketPost(@RequestBody ScrapRequestDTO scrapRequestDTO) {
        scrapService.scrapMarketPost(scrapRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 2. 스크랩한 모든 물품글 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<ScrapResponseDTO>> getScrappedMarketPosts(@PathVariable Long userId) {
        List<ScrapResponseDTO> posts = scrapService.getScrappedMarketPosts(userId);
        return ResponseEntity.ok(posts);
    }

    // 3. 스크랩 목록에서 특정 물품글 스크랩 취소 (삭제)
    @DeleteMapping("/{userId}/{marketPostId}")
    public ResponseEntity<Void> deleteScrap(@PathVariable Long userId, @PathVariable Long marketPostId) {
        scrapService.deleteMarketPost(userId, marketPostId);
        return ResponseEntity.noContent().build();
    }
}