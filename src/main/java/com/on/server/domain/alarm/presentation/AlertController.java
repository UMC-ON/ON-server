package com.on.server.domain.alarm.presentation;

import com.on.server.domain.alarm.application.AlertService;
import com.on.server.domain.alarm.dto.AlertListResponseDto;
import com.on.server.domain.alarm.dto.DeviceTokenRequestDto;
import com.on.server.domain.user.application.UserService;
import com.on.server.domain.user.domain.User;
import com.on.server.global.common.CommonResponse;
import com.on.server.global.common.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "알림 기록 리스트")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alert")
public class AlertController {

    private final AlertService alertService;

    private final UserService userService;

    // 사용자 디바이스 토큰 저장하기
    @PostMapping("/deviceToken")
    @Operation(summary = "사용자 디바이스 토큰 저장하기")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
    public CommonResponse<?> saveDeviceToken(@AuthenticationPrincipal UserDetails userDetails, @RequestBody DeviceTokenRequestDto deviceTokenRequestDto) {
        User user = userService.getUserByUserDetails(userDetails);

        alertService.saveDeviceToken(user, deviceTokenRequestDto);

        return new CommonResponse<>(ResponseCode.SUCCESS);
    }

    // 알림 리스트 조회
    @GetMapping("/list")
    @Operation(summary = "사용자 알림 리스트 조회하기")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
    public CommonResponse<?> getAlertList(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByUserDetails(userDetails);

        List<AlertListResponseDto> alertResponseDtoList = alertService.getAlertList(user);

        return new CommonResponse<>(ResponseCode.SUCCESS, alertResponseDtoList);
    }

}