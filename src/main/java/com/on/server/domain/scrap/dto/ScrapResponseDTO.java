package com.on.server.domain.scrap.dto;

import com.on.server.domain.marketPost.dto.MarketPostResponseDTO;
import com.on.server.domain.scrap.domain.Scrap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScrapResponseDTO {
    // 스크랩 ID
    private Long scrapId;

    // 유저 ID
    private Long userId; // 스크랩한 사용자의 ID

    // 물품거래글에 대한 정보
    private MarketPostResponseDTO marketPost; // 물품거래글 작성자의 ID

    public static ScrapResponseDTO from(Scrap scrap) {
        return ScrapResponseDTO.builder()
                .scrapId(scrap.getId())
                .userId(scrap.getUser().getId())
                .marketPost(MarketPostResponseDTO.from(scrap.getMarketPost())) // MarketPostResponseDTO의 from 메서드를 사용
                .build();
    }
}