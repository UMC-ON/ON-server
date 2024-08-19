package com.on.server.domain.home.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class InfoBoardListResponseDto {
    private String title;
    private String content;

    private String postTime;
    private String postImg;

    private String writer; // 익명이면 익명, 익명 아니면 nickname
    private Integer commentCount;
}
