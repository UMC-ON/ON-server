package com.on.server.domain.diary.presentation;

import com.on.server.domain.diary.application.DiaryService;
import com.on.server.domain.diary.dto.DiaryRequestDto;
import com.on.server.domain.user.domain.User;
import com.on.server.global.common.CommonResponse;
import com.on.server.global.common.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "일기 작성")
@RestController
@RequiredArgsConstructor
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryService diaryService;

    // 1. 일기 리스트 보여주는 일기 홈
    @GetMapping("/list")
    @Operation(summary = "일기 홈 조회")
    public CommonResponse<?> getDiaryHome(@AuthenticationPrincipal User user) {

        return new CommonResponse<>(ResponseCode.SUCCESS, diaryService.getDiary(user));
    }

    // 2, 일기 작성하기
    @PostMapping()
    @Operation(summary = "일기 작성하기")
    public CommonResponse<?> createDiary(@AuthenticationPrincipal User user, @RequestBody DiaryRequestDto diaryRequestDto) {

        diaryService.createDiary(user, diaryRequestDto);

        return new CommonResponse<>(ResponseCode.SUCCESS);
    }
}
