package com.on.server.domain.diary.application;

import com.on.server.domain.diary.domain.Diary;
import com.on.server.domain.diary.domain.repository.DiaryRepository;
import com.on.server.domain.diary.dto.DiaryDto;
import com.on.server.domain.diary.dto.DiaryRequestDto;
import com.on.server.domain.diary.dto.DiaryResponseDto;
import com.on.server.domain.diary.dto.StartDateRequestDto;
import com.on.server.domain.user.domain.User;
import com.on.server.domain.user.domain.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryService {

    private final DiaryRepository diaryRepository;

    private final UserRepository userRepository;

    // 0. 교환 시작 날짜 설정하기
    @Transactional
    public void setStartDate(User user, StartDateRequestDto startDateRequestDto) {
        user.setStartDate(startDateRequestDto.getStartDate());
        userRepository.save(user);
    }

    // 1. 일기 홈 조회하기
    public DiaryResponseDto getDiary(User user) {

        // user 일기 목록 찾기
        List<Diary> diaryList = diaryRepository.findByUser(user);

        // d-day 계산하기
        // 2일 남은 경우 = 2, 당일인 경우 = 0, 2일 지난 경우 = -2
        LocalDate startDate = user.getStartDate();

        Long dDayNow;
        if(startDate == null) { //startDate 비어있는 경우
            dDayNow = null;
        }
        else { //startDate 저장되어있는 경우
            dDayNow = ChronoUnit.DAYS.between(LocalDate.now(), startDate);
        }

        // diaryDto List 만들기
        List<DiaryDto> diaryDtoList = getDiaryDto(diaryList);

        // date List 만들기
        List<LocalDate> dateList = diaryDtoList.stream()
                .map(DiaryDto::getWrittenDate)
                .collect(Collectors.toList());

        return new DiaryResponseDto(user.getDispatchedUniversity(), dDayNow, diaryDtoList, dateList);
    }

    private static List<DiaryDto> getDiaryDto(List<Diary> diaryList) {
        return diaryList.stream()
                .map(diary -> new DiaryDto(
                        diary.getWrittenDate(),
                        diary.getContent(),
                        diary.getDDay())).toList();
    }

    // 2. 일기 등록하기
    @Transactional
    public void createDiary(User user, DiaryRequestDto diaryRequestDto) {
        // d-day 계산
        // startDate 임시 설정
        LocalDate startDate = user.getStartDate();

        Long dDay;
        if(startDate == null) { //startDate 비어있는 경우
            dDay = null;
        }
        else { //startDate 저장되어있는 경우
            dDay = ChronoUnit.DAYS.between(diaryRequestDto.getDate(), startDate);
        }


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
