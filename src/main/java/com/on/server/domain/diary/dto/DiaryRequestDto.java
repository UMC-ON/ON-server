package com.on.server.domain.diary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DiaryRequestDto {

    private LocalDate date;

    private String content;

    private int dDay;
}