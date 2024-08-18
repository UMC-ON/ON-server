package com.on.server.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CompanyChatResponseDto {
    private boolean isFullyRecruited; // 모집 다 되었는지 여부
    private Long periodDay; // 예상 일정 일 횟수
    private LocalDate startDate; // 희망시기 시작
    private LocalDate endDate; // 희망시기 끝
    private String location; // 장소
    private Long recruitNumber; // 전체 모집 인원
    private Long participantNumber; // 현재 모집 인원
}
