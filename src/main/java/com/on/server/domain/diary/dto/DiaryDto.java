package com.on.server.domain.diary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DiaryDto {

    private LocalDate writtenDate;

    private String content;

    private Long writtenDday;
}