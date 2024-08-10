package com.on.server.domain.companyPost.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class CompanyPostRequestDTO {

    // 작성자 ID
    private Long userId;

    //나이 비공개 여부
    private boolean ageAnonymous;

    //파견교 비공개 여부
    private boolean universityAnonymous;

    //제목
    private String title;

    //내용
    private String content;

    //출발지
    private String departurePlace;

    //도착지
    private String arrivePlace;

    //모집 인원 수
    private Long recruitNumber;

    //예정 일정 기간 일과 횟수
    private Long schedulePeriodDay;

    //희망 시기 시작
    private LocalDate startDate;

    //희망 시기 끝
    private LocalDate endDate;

    // 이미지 ID 리스트
    private List<Long> imageIds;

}