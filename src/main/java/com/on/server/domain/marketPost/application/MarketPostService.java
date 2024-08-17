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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class MarketPostService {

    private final MarketPostRepository marketPostRepository;
    private final UserRepository userRepository;
    private final UuidFileService uuidFileService;

    // 1. 모든 물품글 조회
    @Transactional(readOnly = true)
    public List<MarketPostResponseDTO> getAllMarketPosts() {
        return marketPostRepository.findAll().stream()
                .map(this::mapToMarketPostResponseDTO)
                .collect(Collectors.toList());
    }

    // 2. 특정 물품글 조회
    @Transactional(readOnly = true)
    public MarketPostResponseDTO getMarketPostById(Long marketPostId) {
        MarketPost marketPost = marketPostRepository.findById(marketPostId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다. ID: " + marketPostId));
        return mapToMarketPostResponseDTO(marketPost);
    }

    // 3. 새로운 물품글 작성
    public MarketPostResponseDTO createMarketPost(MarketPostRequestDTO requestDTO) {
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + requestDTO.getUserId()));

        MarketPost marketPost = MarketPost.builder()
                .user(user)
                .title(requestDTO.getTitle())
                .isShare(requestDTO.isShare())
                .cost(requestDTO.getCost())
                .dealType(requestDTO.getDealType())
                .dealStatus(requestDTO.getDealStatus())
                .content(requestDTO.getContent())
                .currentCountry(requestDTO.getCurrentCountry())
                .currentLocation(requestDTO.getCurrentLocation())
                .images(new ArrayList<>()) // 이미지 리스트 초기화
                .build();

        marketPost = marketPostRepository.saveAndFlush(marketPost);

        List<UuidFile> uploadedImages = requestDTO.getImageFiles().stream()
                .map(file -> {
                    UuidFile savedFile = uuidFileService.saveFile(file, FilePath.POST);
                    if (savedFile.getId() == null) {
                        throw new RuntimeException("UuidFile 저장 중 ID가 생성되지 않았습니다.");
                    }
                    return savedFile;
                })
                .collect(Collectors.toList());

        marketPost.getImages().addAll(uploadedImages);

        marketPost = marketPostRepository.saveAndFlush(marketPost);

        return mapToMarketPostResponseDTO(marketPost);
    }

    // 4. 특정 사용자가 작성한 모든 물품글 조회
    @Transactional(readOnly = true)
    public List<MarketPostResponseDTO> getMarketPostsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + userId));

        return marketPostRepository.findByUser(user).stream()
                .map(this::mapToMarketPostResponseDTO)
                .collect(Collectors.toList());
    }

    // 5. 특정 게시글 삭제
    public void deleteMarketPost(Long userId, Long marketPostId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + userId));
        MarketPost marketPost = marketPostRepository.findById(marketPostId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다. ID: " + marketPostId));

        if (!marketPost.getUser().getId().equals(userId)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        // 연관된 이미지 삭제
        marketPost.getImages().forEach(uuidFileService::deleteFile);

        marketPostRepository.delete(marketPost);
    }

    // 6. 거래 상태 업데이트
    public MarketPostResponseDTO updateMarketPostStatus(Long marketPostId, DealStatus status) {
        MarketPost marketPost = marketPostRepository.findById(marketPostId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다. ID: " + marketPostId));

        marketPost.setDealStatus(status); // 상태 업데이트
        MarketPost updatedMarketPost = marketPostRepository.save(marketPost); // 저장

        return mapToMarketPostResponseDTO(updatedMarketPost);
    }

    // 필터링: 거래형식, 국가 필터링
    @Transactional(readOnly = true)
    public List<MarketPostResponseDTO> getFilteredMarketPosts(DealType dealType, String currentCountry) {
        return marketPostRepository.findFilteredMarketPosts(dealType, currentCountry, null).stream()
                .map(this::mapToMarketPostResponseDTO)
                .collect(Collectors.toList());
    }

    // 필터링: 거래 가능 물품만 보기
    @Transactional(readOnly = true)
    public List<MarketPostResponseDTO> getAvailableMarketPosts() {
        return marketPostRepository.findFilteredMarketPosts(null, null, DealStatus.AWAIT).stream()
                .map(this::mapToMarketPostResponseDTO)
                .collect(Collectors.toList());
    }

    // 검색 기능
    @Transactional(readOnly = true)
    public List<MarketPostResponseDTO> searchMarketPosts(String keyword) {
        List<MarketPost> marketPosts = marketPostRepository.searchMarketPosts(keyword);
        return marketPosts.stream()
                .map(marketPost -> mapToMarketPostResponseDTO(marketPost))
                .collect(Collectors.toList());
    }

    // MarketPost 엔티티를 MarketPostResponseDto로 매핑하는 메서드
    private MarketPostResponseDTO mapToMarketPostResponseDTO(MarketPost marketPost) {
        return MarketPostResponseDTO.builder()
                .marketPostId(marketPost.getId())
                .userId(marketPost.getUser().getId())
                .nickname(marketPost.getUser().getNickname())
                .currentCountry(marketPost.getCurrentCountry())
                .currentLocation(marketPost.getCurrentLocation())
                .title(marketPost.getTitle())
                .cost(marketPost.getCost())
                .isShare(marketPost.isShare())
                .dealType(marketPost.getDealType())
                .dealStatus(marketPost.getDealStatus())
                .content(marketPost.getContent())
                .imageUrls(marketPost.getImages().stream().map(UuidFile::getFileUrl).collect(Collectors.toList())) // 이미지 URL 리스트
                .build();
    }
}
