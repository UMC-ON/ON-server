package com.on.server.domain.scrap.application;

import com.on.server.domain.marketPost.domain.MarketPost;
import com.on.server.domain.marketPost.domain.repository.MarketPostRepository;
import com.on.server.domain.marketPost.dto.MarketPostResponseDTO;
import com.on.server.domain.scrap.domain.Scrap;
import com.on.server.domain.scrap.domain.repository.ScrapRepository;
import com.on.server.domain.scrap.dto.ScrapRequestDTO;
import com.on.server.domain.scrap.dto.ScrapResponseDTO;
import com.on.server.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final UserRepository userRepository;
    private final MarketPostRepository marketPostRepository;

    // 1. 특정 물품글 스크랩
    public void scrapMarketPost(ScrapRequestDTO scrapRequestDTO) {
    }

    // 2. 스크랩한 모든 물품글 조회
    @Transactional(readOnly = true)
    public List<ScrapResponseDTO> getScrappedMarketPosts(Long userId) {
        return null;
    }

    // 3. 스크랩 목록에서 자기가 작성한 특정 물품글 스크랩 취소 (삭제)
    public void deleteMarketPost(Long userId, Long marketPostId) {
    }

}
