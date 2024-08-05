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
        return null;
    }

    // 2. 특정 물품글 조회
    @Transactional(readOnly = true)
    public MarketPostResponseDTO getMarketPostById(Long marketPostId) {
        return null;
    }

    // 3. 새로운 물품글 작성
    public MarketPostResponseDTO createMarketPost(MarketPostRequestDTO requestDTO) {
        return null;
    }

    // 4. 특정 사용자가 작성한 모든 물품글 조회
    @Transactional(readOnly = true)
    public List<MarketPostResponseDTO> getMarketPostsByUserId(Long userId) {
        return null;
    }
}
