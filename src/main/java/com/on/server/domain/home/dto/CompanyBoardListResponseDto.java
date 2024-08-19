package com.on.server.domain.home.dto;

import com.on.server.domain.user.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class CompanyBoardListResponseDto {
    private String postImg;

    private String title;

    private String nickname;

    private boolean ageAnonymous;

    private Integer age;

    private Gender gender;

    private LocalDate startDate;

    private LocalDate endDate;

    private Long currentRecruitNumber;

    private Long totalRecruitNumber;

    private List<String> travelPlace;
}
