package com.on.server.domain.companyPost.dto;

import com.on.server.domain.user.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyPostResponseDTO {

    // 동행 구하기 글 ID
    private Long companyPostId;

    // 작성자 ID
    private Long userId;

    // 작성자 나이
    private Integer age;

    // 나이 비공개 여부
    private boolean ageAnonymous;

    // 작성자 파견교 이름
    private String dispatchedUniversity;

    // 작성자 닉네임
    private String nickname;

    // 작성자 성별
    private Gender gender;

    // 파견교 비공개 여부
    private boolean universityAnonymous;

    // 현재 국가
    private String country;

    // 제목
    private String title;

    // 내용
    private String content;

    // 여행 지역
    private List<String> travelArea;

    // 현재 모집 인원 수
    private Long currentRecruitNumber;

    // 전체 모집 인원 수
    private Long totalRecruitNumber;

    // 예정 일정 기간
    private Long schedulePeriodDay;

    // 희망 시기 시작
    private LocalDate startDate;

    // 희망 시기 끝
    private LocalDate endDate;

    // 이미지 URL 리스트
    private List<String> imageUrls;

    // 작성 시간
    private LocalDateTime createdAt;
}
