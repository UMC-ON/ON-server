package com.on.server.domain.marketPost.dto;

import com.on.server.domain.marketPost.domain.DealStatus;
import com.on.server.domain.marketPost.domain.DealType;
import com.on.server.domain.marketPost.domain.MarketPost;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MarketPostRequestDTO {

    // 작성자 ID
    private Long userId;

    // 제목
    private String title;

    // 공유 여부
    private boolean isShare;

    // 비용
    private Long cost;

    // 거래 유형
    private DealType dealType;

    // 거래 상태
    private DealStatus dealStatus;

    // 상품 설명
    private String content;

    // 이미지 ID 리스트
    private List<Long> imageIdList;

    // 현재 위치 국가
    private String currentCountry;

    // 현재 위치 지역
    private String currentLocation;
}
