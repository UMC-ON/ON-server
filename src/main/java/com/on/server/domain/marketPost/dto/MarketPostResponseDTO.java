package com.on.server.domain.marketPost.dto;

import com.on.server.domain.marketPost.domain.MarketPost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    // 국가 ID
    private Long countryId;

    // 지역 ID
    private Long locationId;

    // 제목
    private String title;

    // 판매 금액
    private Long cost;

    // 나눔 여부
    private boolean isShare;

    // 거래 형식
    private MarketPost.DealType dealType;

    // 거래 여부MarketPost.
    private MarketPost.DealStatus dealStatus;

    // 상품 설명
    private String content;

    // 이미지 ID 리스트
    private List<Long> imageIdList;

}
