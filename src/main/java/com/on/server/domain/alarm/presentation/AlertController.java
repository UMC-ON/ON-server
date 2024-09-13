package com.on.server.domain.alarm.presentation;

import com.on.server.domain.alarm.application.AlertService;
import com.on.server.domain.alarm.dto.AlertListResponseDto;
import com.on.server.domain.alarm.dto.AlertUrlDto;
import com.on.server.domain.alarm.dto.DeviceTokenRequestDto;
import com.on.server.domain.user.domain.User;
import com.on.server.global.security.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "알림 기록 리스트")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alert")
public class AlertController {

    private final AlertService alertService;

    private final SecurityService securityService;

    // 사용자 디바이스 토큰 저장하기
    @PostMapping("/deviceToken")
    @Operation(summary = "사용자 디바이스 토큰 저장하기")
    @PreAuthorize("@securityService.isNotTemporaryUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
    public ResponseEntity<?> saveDeviceToken(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody DeviceTokenRequestDto deviceTokenRequestDto
    ) {
        User user = securityService.getUserByUserDetails(userDetails);

        alertService.saveDeviceToken(user, deviceTokenRequestDto);

        return ResponseEntity.ok().build();
    }

    // 알림 리스트 조회
    @GetMapping("/list")
    @Operation(summary = "사용자 알림 리스트 조회하기")
    @PreAuthorize("@securityService.isNotTemporaryUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
    public ResponseEntity<?> getAlertList(
            @AuthenticationPrincipal UserDetails userDetails,
            @ParameterObject Pageable pageable
    ) {
        User user = securityService.getUserByUserDetails(userDetails);

        List<AlertListResponseDto> alertResponseDtoList = alertService.getAlertList(user, pageable);

        return ResponseEntity.ok(alertResponseDtoList);

    }

    // 알림 이동 페이지 조회
    @PostMapping("/{alertId}")
    @Operation(summary = "사용자 알림 읽음 상태 업데이트 및 URL 반환")
    @PreAuthorize("@securityService.isNotTemporaryUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
    public ResponseEntity<?> getRedirect(@PathVariable Long alertId) {

        // 알림 읽음으로 처리하기
        AlertUrlDto alertUrlDto = alertService.markAsReadAndRedirect(alertId);

        return ResponseEntity.created(URI.create(alertUrlDto.getApiUrl() + alertUrlDto.getConnectId())).build();
    }


}