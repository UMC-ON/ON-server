package com.on.server.domain.companyPost.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CompanyPostRequestDTO {

    // 작성자 ID
    private Long userId;

    //나이 비공개 여부
    private boolean ageAnonymous;

    //파견교 비공개 여부
    private boolean universityAnonymous;

    // 현재 국가
    private String currentCountry;

    //제목
    private String title;

    //내용
    private String content;

    // 여행 지역
    private List<String> travelArea;

    // 전체 모집 인원 수
    private Long totalRecruitNumber;

    // 예상 일정 기간
    private Long schedulePeriodDay;

    // 희망 시기 시작
    private LocalDate startDate;

    // 희망 시기 끝
    private LocalDate endDate;

    // 이미지 파일 리스트
    //private List<MultipartFile> imageFiles;

}