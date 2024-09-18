package com.on.server.domain.chat.dto.response;

import com.on.server.domain.marketPost.domain.DealType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MarketChatDto {
    private String productName; // 상품 이름
    private Long productPrice; // 상품 가격
    private DealType tradeMethod; // 거래 방법
    private String imageUrl; // 상품 이미지 url
}
