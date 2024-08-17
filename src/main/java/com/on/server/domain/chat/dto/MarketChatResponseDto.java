package com.on.server.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MarketChatResponseDto {
    private String productName; // 상품 이름
    private int productPrice; // 상품 가격
    private String tradeMethod; // 거래 방법
    private String imageUrl; // 상품 이미지 url
}
