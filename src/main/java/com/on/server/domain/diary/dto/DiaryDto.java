package com.on.server.domain.diary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DiaryDto {

    private LocalDate writtenDate;

    private String content;

    private Long writtenDday;
}