package com.on.server.domain.marketPost.dto;

import com.on.server.domain.marketPost.domain.DealStatus;
import com.on.server.domain.marketPost.domain.DealType;
import com.on.server.domain.marketPost.domain.MarketPost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarketPostResponseDTO {

    // 중고거래 글 ID
    private Long marketPostId;

    // 작성자 ID
    private Long userId;

    // 작성자 닉네임
    private String nickname;

    // 현재 위치 국가
    private String currentCountry;

    // 현재 위치 지역
    private String currentLocation;

    // 제목
    private String title;

    // 판매 금액
    private Long cost;

    // 나눔 여부
    private boolean isShare;

    // 거래 형식
    private DealType dealType;

    // 거래 여부
    private DealStatus dealStatus;

    // 상품 설명
    private String content;

    // 이미지 URL 리스트
    private List<String> imageUrls;

    // 작성 시간
    private LocalDateTime createdAt;

}
