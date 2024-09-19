package com.on.server.domain.diary.presentation;

import com.on.server.domain.diary.application.DiaryService;
import com.on.server.domain.diary.dto.DiaryRequestDto;
import com.on.server.domain.diary.dto.StartDateRequestDto;
import com.on.server.domain.user.domain.User;
import com.on.server.global.security.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "일기 작성")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/diary")
public class DiaryController {

    private final DiaryService diaryService;

    private final SecurityService securityService;

    // 0. 날짜 설정하기
    @PostMapping("/startdate")
    @Operation(summary = "시작 날짜 설정")
    @PreAuthorize("@securityService.isNotTemporaryUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
    public ResponseEntity<?> setStartDate(@AuthenticationPrincipal UserDetails userDetails,
                                          @RequestBody StartDateRequestDto startDateRequestDto) {
        User user = securityService.getUserByUserDetails(userDetails);
        diaryService.setStartDate(user, startDateRequestDto);

        return ResponseEntity.ok().build();
    }

    // 1. 일기 리스트 보여주는 일기 홈
    @GetMapping("/list")
    @Operation(summary = "일기 홈 조회", description = "D-Day 계산 : 2일 남은 경우 = 2, 당일인 경우 = 0, 2일 지난 경우 = -2")
    @PreAuthorize("@securityService.isNotTemporaryUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
    public ResponseEntity<?> getDiaryHome(@AuthenticationPrincipal UserDetails userDetails) {

        User user = securityService.getUserByUserDetails(userDetails);
        return ResponseEntity.ok(diaryService.getDiary(user));
    }

    // 2, 일기 작성하기
    @PostMapping()
    @Operation(summary = "일기 작성하기")
    @PreAuthorize("@securityService.isNotTemporaryUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
    public ResponseEntity<?> createDiary(@AuthenticationPrincipal UserDetails userDetails,
                                         @RequestBody DiaryRequestDto diaryRequestDto) {

        User user = securityService.getUserByUserDetails(userDetails);
        diaryService.createDiary(user, diaryRequestDto);

        return ResponseEntity.ok().build();
    }
}
