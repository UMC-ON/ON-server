package com.on.server.domain.scrap.application;

import com.on.server.domain.marketPost.domain.MarketPost;
import com.on.server.domain.marketPost.domain.repository.MarketPostRepository;
import com.on.server.domain.scrap.domain.Scrap;
import com.on.server.domain.scrap.domain.repository.ScrapRepository;
import com.on.server.domain.scrap.dto.ScrapRequestDTO;
import com.on.server.domain.scrap.dto.ScrapResponseDTO;
import com.on.server.domain.user.domain.User;
import com.on.server.global.common.ResponseCode;
import com.on.server.global.common.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final MarketPostRepository marketPostRepository;

    // 1. 특정 물품글 스크랩
    public void scrapMarketPost(User user, ScrapRequestDTO scrapRequestDTO) {
        MarketPost marketPost = marketPostRepository.findById(scrapRequestDTO.getMarketPostId())
                .orElseThrow(() -> new BadRequestException(ResponseCode.ROW_DOES_NOT_EXIST, "게시글을 찾을 수 없습니다. ID: " + scrapRequestDTO.getMarketPostId()));

        // 중복 스크랩 방지하기
        boolean alreadyScrapped = scrapRepository.findByUserAndMarketPost(user, marketPost).isPresent();
        if (alreadyScrapped) {
            throw new BadRequestException(ResponseCode.ROW_ALREADY_EXIST, "이미 스크랩된 게시글입니다.");
        }

        // 스크랩 로직 구현
        Scrap scrap = Scrap.builder()
                .user(user)
                .marketPost(marketPost)
                .build();

        scrapRepository.save(scrap);
    }

    // 2. 스크랩한 모든 물품글 조회
    @Transactional(readOnly = true)
    public Page<ScrapResponseDTO> getScrappedMarketPosts(User user, Pageable pageable) {
        return scrapRepository.findByUser(user, pageable)
                .map(ScrapResponseDTO::from);
    }

    // 3. 스크랩 목록에서 자기가 작성한 특정 물품글 스크랩 취소 (삭제)
    public void deleteMarketPost(User user, Long marketPostId) {
        MarketPost marketPost = marketPostRepository.findById(marketPostId)
                .orElseThrow(() -> new BadRequestException(ResponseCode.ROW_DOES_NOT_EXIST, "게시글을 찾을 수 없습니다. ID: " + marketPostId));

        Scrap scrap = scrapRepository.findByUserAndMarketPost(user, marketPost)
                .orElseThrow(() -> new BadRequestException(ResponseCode.ROW_DOES_NOT_EXIST, "스크랩을 찾을 수 없습니다."));

        scrapRepository.delete(scrap);
    }
}
