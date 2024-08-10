package com.on.server.domain.marketPost.application;

import com.on.server.domain.country.Country;
import com.on.server.domain.marketPost.domain.MarketPost;
import com.on.server.domain.marketPost.domain.repository.MarketPostRepository;
import com.on.server.domain.marketPost.dto.MarketPostRequestDTO;
import com.on.server.domain.marketPost.dto.MarketPostResponseDTO;
import com.on.server.domain.user.domain.User;
import com.on.server.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class MarketPostService {

    private final MarketPostRepository marketPostRepository;
    private final UserRepository userRepository;
//    private final ImageRepository imageRepository;
//    private final CountryRepository countryRepository;
//    private final LocationRepository locationRepository;

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
        //Country country = countryRepository.findById(requestDTO.getCountryId())
                //.orElseThrow(() -> new RuntimeException("국가를 찾을 수 없습니다. ID: " + requestDTO.getCountryId()));
        //Location location = locationRepository.findById(requestDTO.getLocationId())
                //.orElseThrow(() -> new RuntimeException("위치를 찾을 수 없습니다. ID: " + requestDTO.getLocationId()));

        MarketPost marketPost = MarketPost.builder()
                .user(user)
                .title(requestDTO.getTitle())
                .isShare(requestDTO.isShare())
                .cost(requestDTO.getCost())
                .dealType(requestDTO.getDealType())
                .dealStatus(requestDTO.getDealStatus())
                .content(requestDTO.getContent())
//                .images(images)
//                .country(country)
//                .location(location)
                .build();

        marketPostRepository.save(marketPost);

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

    // MarketPost 엔티티를 MarketPostResponseDto로 매핑하는 메서드
    private MarketPostResponseDTO mapToMarketPostResponseDTO(MarketPost marketPost) {
        return MarketPostResponseDTO.builder()
                .marketPostId(marketPost.getId())
                .userId(marketPost.getUser().getId())
                .countryId(marketPost.getCountry().getId())
                //.locationId(marketPost.getLocation().getLocationId())
                .title(marketPost.getTitle())
                .cost(marketPost.getCost())
                .isShare(marketPost.isShare())
                .dealType(marketPost.getDealType())
                .dealStatus(marketPost.getDealStatus())
                .content(marketPost.getContent())
                //.imageIdList(marketPost.getImages().stream().map(Image::getImageId).collect(Collectors.toList()))
                .build();
    }
}
