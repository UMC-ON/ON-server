package com.on.server.domain.diary.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StartDateRequestDto {

    private LocalDate startDate;

}
