package com.on.server.domain.scrap.application;

import com.on.server.domain.marketPost.domain.MarketPost;
import com.on.server.domain.marketPost.domain.repository.MarketPostRepository;
import com.on.server.domain.marketPost.dto.MarketPostResponseDTO;
import com.on.server.domain.scrap.domain.Scrap;
import com.on.server.domain.scrap.domain.repository.ScrapRepository;
import com.on.server.domain.scrap.dto.ScrapRequestDTO;
import com.on.server.domain.scrap.dto.ScrapResponseDTO;
import com.on.server.domain.user.domain.User;
import com.on.server.domain.user.domain.repository.UserRepository;
import com.on.server.global.aws.s3.uuidFile.domain.UuidFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final UserRepository userRepository;
    private final MarketPostRepository marketPostRepository;

    // 1. 특정 물품글 스크랩
    public void scrapMarketPost(ScrapRequestDTO scrapRequestDTO) {
        User user = userRepository.findById(scrapRequestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + scrapRequestDTO.getUserId()));
        MarketPost marketPost = marketPostRepository.findById(scrapRequestDTO.getMarketPostId())
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다. ID: " + scrapRequestDTO.getMarketPostId()));

        // 중복 스크랩 방지하기
        boolean alreadyScrapped = scrapRepository.findByUserAndMarketPost(user, marketPost).isPresent();
        if (alreadyScrapped) {
            throw new RuntimeException("이미 스크랩된 게시글입니다.");
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
    public List<ScrapResponseDTO> getScrappedMarketPosts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + userId));

        // 스크랩한 물품글 목록을 가져오고, 이를 ScrapResponseDTO로 변환
        return scrapRepository.findByUser(user).stream()
                .map(this::mapToScrapResponseDTO)  // Scrap 객체를 ScrapResponseDTO로 매핑
                .collect(Collectors.toList());
    }

    // 3. 스크랩 목록에서 자기가 작성한 특정 물품글 스크랩 취소 (삭제)
    public void deleteMarketPost(Long userId, Long marketPostId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + userId));
        MarketPost marketPost = marketPostRepository.findById(marketPostId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다. ID: " + marketPostId));
        Scrap scrap = scrapRepository.findByUserAndMarketPost(user, marketPost)
                .orElseThrow(() -> new RuntimeException("스크랩을 찾을 수 없습니다."));

        scrapRepository.delete(scrap);
    }

    // Scrap 엔티티를 ScrapResponseDto로 매핑하는 메서드
    private ScrapResponseDTO mapToScrapResponseDTO(Scrap scrap) {
        return ScrapResponseDTO.builder()
                .scrapId(scrap.getId())
                .userId(scrap.getUser().getId())
                .marketPost(mapToMarketPostResponseDTO(scrap.getMarketPost()))
                .build();
    }

    // MarketPost 엔티티를 MarketPostResponseDTO로 매핑하는 메서드
    private MarketPostResponseDTO mapToMarketPostResponseDTO(MarketPost marketPost) {
        return MarketPostResponseDTO.builder()
                .marketPostId(marketPost.getId())
                .userId(marketPost.getUser().getId())
                .currentCountry(marketPost.getCurrentCountry())
                .currentLocation(marketPost.getCurrentLocation())
                .title(marketPost.getTitle())
                .cost(marketPost.getCost())
                .isShare(marketPost.isShare())
                .dealType(marketPost.getDealType())
                .dealStatus(marketPost.getDealStatus())
                .content(marketPost.getContent())
                .imageUrls(marketPost.getImages().stream()
                        .map(UuidFile::getFileUrl) // 이미지 URL 리스트를 가져오는 로직
                        .collect(Collectors.toList()))
                .build();
    }
}
