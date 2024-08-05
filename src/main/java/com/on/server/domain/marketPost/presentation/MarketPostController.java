package com.on.server.domain.marketPost.presentation;

import com.on.server.domain.marketPost.application.MarketPostService;
import com.on.server.domain.marketPost.dto.MarketPostRequestDTO;
import com.on.server.domain.marketPost.dto.MarketPostResponseDTO;
import com.on.server.domain.scrap.application.ScrapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/market-posts")
public class MarketPostController {

    private final MarketPostService marketPostService;
    private final ScrapService scrapService;

    @Autowired
    public MarketPostController(MarketPostService marketPostService, ScrapService scrapService) {
        this.marketPostService = marketPostService;
        this.scrapService = scrapService;
    }

    // 1. 모든 물품글 조회
    @GetMapping
    public ResponseEntity<List<MarketPostResponseDTO>> getAllMarketPosts() {
        List<MarketPostResponseDTO> posts = marketPostService.getAllMarketPosts();
        return ResponseEntity.ok(posts);
    }

    // 2. 특정 물품글 조회
    @GetMapping("/{marketPostId}")
    public ResponseEntity<MarketPostResponseDTO> getMarketPostById(@PathVariable Long marketPostId) {
        MarketPostResponseDTO post = marketPostService.getMarketPostById(marketPostId);
        return ResponseEntity.ok(post);
    }

    // 3. 새로운 물품글 작성
    @PostMapping
    public ResponseEntity<MarketPostResponseDTO> createMarketPost(@RequestBody MarketPostRequestDTO requestDTO) {
        MarketPostResponseDTO createdPost = marketPostService.createMarketPost(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    // 4. 마이페이지에서 자기가 작성한 모든 물품글 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MarketPostResponseDTO>> getMarketPostsByUserId(@PathVariable Long userId) {
        List<MarketPostResponseDTO> posts = marketPostService.getMarketPostsByUserId(userId);
        return ResponseEntity.ok(posts);
    }
}