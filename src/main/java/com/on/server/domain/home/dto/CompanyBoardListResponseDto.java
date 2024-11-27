package com.on.server.domain.home.dto;

import com.on.server.domain.companyPost.domain.CompanyPost;
import com.on.server.domain.user.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CompanyBoardListResponseDto {
    private Long postId;

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

    private List<String> travelArea;


    public static CompanyBoardListResponseDto getCompanyBoardDto(CompanyPost companyPost) {
        return CompanyBoardListResponseDto.builder()
                .postId(companyPost.getId())
                .postImg(companyPost.getImages().isEmpty() ? null : companyPost.getImages().get(0).getFileUrl())
                .title(companyPost.getTitle())
                .nickname(companyPost.getUser().getNickname())
                .ageAnonymous(companyPost.isAgeAnonymous())
                .age(companyPost.getUser().getAge())
                .gender(companyPost.getUser().getGender())
                .startDate(companyPost.getStartDate())
                .endDate(companyPost.getEndDate())
                .currentRecruitNumber(companyPost.getCurrentRecruitNumber())
                .totalRecruitNumber(companyPost.getTotalRecruitNumber())
                .travelArea(companyPost.getTravelArea())
                .build();
    }
}
