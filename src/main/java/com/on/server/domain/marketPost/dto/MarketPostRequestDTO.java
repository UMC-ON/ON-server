package com.on.server.domain.marketPost.dto;

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
    private MarketPost.DealType dealType;

    // 거래 상태
    private MarketPost.DealStatus dealStatus;

    // 모집 인원 수
    private String content;

    // 이미지 ID 리스트
    private List<Long> imageIdList;

    // 국가 ID
    private Long countryId;

    // 위치 ID
    private Long locationId;
}
