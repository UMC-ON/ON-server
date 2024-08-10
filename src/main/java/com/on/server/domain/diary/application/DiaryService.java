package com.on.server.domain.diary.application;

import com.on.server.domain.diary.domain.Diary;
import com.on.server.domain.diary.domain.repository.DiaryRepository;
import com.on.server.domain.diary.dto.DiaryDto;
import com.on.server.domain.diary.dto.DiaryRequestDto;
import com.on.server.domain.diary.dto.DiaryResponseDto;
import com.on.server.domain.user.domain.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaryService {

    private final DiaryRepository diaryRepository;

    // 1. 일기 홈 조회하기
    public DiaryResponseDto getDiary(User user) {

        //user 일기 목록 찾기
        List<Diary> diaryList = diaryRepository.findByUser(user);

        //d-day 계산하기
        Long dDayNow = ChronoUnit.DAYS.between(user.getStartDate(), LocalDate.now());

        //diaryDto List 만들기
        List<DiaryDto> diaryDtoList = getDiaryDto(diaryList);

        //date List 만들기
        List<LocalDate> dateList = diaryDtoList.stream()
                .map(DiaryDto::getWrittenDate)
                .collect(Collectors.toList());

        return new DiaryResponseDto(user.getUniversity().getName(), dDayNow, diaryDtoList, dateList);
    }

    private static List<DiaryDto> getDiaryDto(List<Diary> diaryList) {
        return diaryList.stream()
                .map(diary -> new DiaryDto(
                        diary.getWrittenDate(),
                        diary.getContent(),
                        diary.getDDay())).toList();
    }

    // 2. 일기 등록하기
    public void createDiary(User user, DiaryRequestDto diaryRequestDto) {
        Long dDay = ChronoUnit.DAYS.between(user.getStartDate(), diaryRequestDto.getDate());

        // 일기 저장하기
        Diary diary = Diary.builder()
                .content(diaryRequestDto.getContent())
                .writtenDate(diaryRequestDto.getDate())
                .dDay(dDay)
                .user(user)
                .build();

        diaryRepository.save(diary);
    }

}
