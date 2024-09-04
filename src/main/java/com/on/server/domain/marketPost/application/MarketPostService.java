package com.on.server.domain.marketPost.application;

import com.on.server.domain.marketPost.domain.DealStatus;
import com.on.server.domain.marketPost.domain.DealType;
import com.on.server.domain.marketPost.domain.MarketPost;
import com.on.server.domain.marketPost.domain.repository.MarketPostRepository;
import com.on.server.domain.marketPost.dto.MarketPostRequestDTO;
import com.on.server.domain.marketPost.dto.MarketPostResponseDTO;
import com.on.server.domain.user.domain.User;
import com.on.server.domain.user.domain.repository.UserRepository;
import com.on.server.global.aws.s3.uuidFile.application.UuidFileService;
import com.on.server.global.aws.s3.uuidFile.domain.FilePath;
import com.on.server.global.aws.s3.uuidFile.domain.UuidFile;
import com.on.server.global.aws.s3.uuidFile.domain.repository.UuidFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MarketPostService {

    private final MarketPostRepository marketPostRepository;
    private final UserRepository userRepository;
    private final UuidFileService uuidFileService;

    // 1. 모든 물품글 조회
    public List<MarketPostResponseDTO> getAllMarketPosts() {
        return marketPostRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(MarketPostResponseDTO::from)
                .collect(Collectors.toList());
    }

    // 2. 특정 물품글 조회
    public MarketPostResponseDTO getMarketPostById(Long marketPostId) {
        MarketPost marketPost = marketPostRepository.findById(marketPostId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다. ID: " + marketPostId));
        return MarketPostResponseDTO.from(marketPost);
    }

    // 3. 새로운 물품글 작성
    @Transactional
    public MarketPostResponseDTO createMarketPost(User user, MarketPostRequestDTO requestDTO, List<MultipartFile> imageFiles) {

        // 이미지 파일 처리
        List<UuidFile> uploadedImages = new ArrayList<>();
        if (imageFiles != null && !imageFiles.isEmpty()) {
            uploadedImages = imageFiles.stream()
                    .map(file -> uuidFileService.saveFile(file, FilePath.POST))
                    .collect(Collectors.toList());
        }

        MarketPost marketPost = MarketPost.builder()
                .user(user)
                .title(requestDTO.getTitle())
                .isShare(requestDTO.isShare())
                .cost(requestDTO.getCost())
                .dealType(requestDTO.getDealType())
                .dealStatus(DealStatus.AWAIT)
                .content(requestDTO.getContent())
                .currentCountry(requestDTO.getCurrentCountry())
                .currentLocation(requestDTO.getCurrentLocation())
                .images(uploadedImages)
                .build();

        marketPost = marketPostRepository.save(marketPost);

        return MarketPostResponseDTO.from(marketPost);
    }

    // 4. 특정 사용자가 작성한 모든 물품글 조회
    public List<MarketPostResponseDTO> getMarketPostsByUser(User user) {
        return marketPostRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(MarketPostResponseDTO::from)
                .collect(Collectors.toList());
    }

    // 5. 특정 게시글 삭제
    @Transactional
    public void deleteMarketPost(User user, Long marketPostId) {
        MarketPost marketPost = marketPostRepository.findById(marketPostId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다. ID: " + marketPostId));

        if (!marketPost.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        // 연관된 이미지 삭제
        marketPost.getImages().forEach(uuidFileService::deleteFile);

        marketPostRepository.delete(marketPost);
    }

    // 6. 거래 상태 업데이트
    @Transactional
    public MarketPostResponseDTO updateMarketPostStatus(Long marketPostId, DealStatus status) {
        MarketPost marketPost = marketPostRepository.findById(marketPostId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다. ID: " + marketPostId));

        marketPost.completeDeal(); // 상태 업데이트

        MarketPost updatedMarketPost = marketPostRepository.save(marketPost); // 저장

        return MarketPostResponseDTO.from(updatedMarketPost);
    }

    // 필터링: 거래형식, 국가, 거래상태 필터링
    public List<MarketPostResponseDTO> getFilteredMarketPosts(DealType dealType, String currentCountry, DealStatus dealStatus) {
        return marketPostRepository.findFilteredMarketPosts(dealType, currentCountry, dealStatus).stream()
                .map(MarketPostResponseDTO::from)
                .collect(Collectors.toList());
    }

    // 필터링: 거래 가능 물품만 보기
    public List<MarketPostResponseDTO> getAvailableMarketPosts() {
        return marketPostRepository.findFilteredMarketPosts(null, null, DealStatus.AWAIT).stream()
                .map(MarketPostResponseDTO::from)
                .collect(Collectors.toList());
    }

    // 검색 기능
    public List<MarketPostResponseDTO> searchMarketPosts(String keyword) {
        List<MarketPost> marketPosts = marketPostRepository.searchMarketPosts(keyword);
        return marketPosts.stream()
                .map(MarketPostResponseDTO::from)
                .collect(Collectors.toList());
    }

    // 내 주변 물품거래글
    // 특정 물품글 조회와 함께 동일한 위치의 다른 게시글 3개 조회
    public List<MarketPostResponseDTO> getNearbyMarketPosts(String currentCountry, Long marketPostId) {
        List<MarketPost> nearbyPosts = marketPostRepository.findTop3ByCurrentCountryAndAwaitingOrder(currentCountry, marketPostId);
        return nearbyPosts.stream()
                .map(MarketPostResponseDTO::from)
                .collect(Collectors.toList());
    }
}
