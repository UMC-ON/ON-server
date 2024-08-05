package com.on.server.domain.scrap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScrapResponseDTO {
    //스크랩 ID
    private Long scrapId;

    //마켓글 ID
    private Long marketPostId;

    //유저 ID
    private Long userId;
}