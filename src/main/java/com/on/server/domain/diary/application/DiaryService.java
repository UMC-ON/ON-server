package com.on.server.domain.diary.application;

import com.on.server.domain.diary.domain.repository.DiaryRepository;
import com.on.server.domain.diary.dto.DiaryRequestDto;
import com.on.server.domain.diary.dto.DiaryResponseDto;
import com.on.server.domain.user.domain.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaryService {

    private final DiaryRepository diaryRepository;

    // 1. 일기 홈 조회하기
    public DiaryResponseDto getDiary(User user) {

        return null;
    }


    // 2. 일기 등록하기
    public void createDiary(User user, DiaryRequestDto diaryRequestDto) {

    }

}
