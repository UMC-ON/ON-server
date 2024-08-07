package com.on.server.domain.diary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class DiaryResponseDto {

    private String univ;

    private int dDay;

    private List<DiaryDto> diaryList;

    private List<LocalDate> dateList; //일기 홈에 기록되어있는 날짜들 ()
}
