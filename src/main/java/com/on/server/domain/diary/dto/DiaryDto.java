package com.on.server.domain.diary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DiaryDto {

    private LocalDate diaryDate;

    private String content;

    private Long writtenDday;
}