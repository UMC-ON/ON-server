package com.on.server.domain.alarm.presentation;

import com.on.server.domain.alarm.application.AlertService;
import com.on.server.domain.alarm.application.FcmService;
import com.on.server.domain.alarm.dto.FcmRequestDto;
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

import java.io.IOException;

@Tag(name = "FCM 알림 테스트")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/fcm/test")
public class FcmTestController {

    private final AlertService alertService;

    private final SecurityService securityService;

    private final FcmService fcmService;

    @PostMapping("/alert")
    @Operation(summary = "알림 생성 테스트")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
    public ResponseEntity pushMessage(@AuthenticationPrincipal UserDetails userDetails) throws IOException {
        User user = securityService.getUserByUserDetails(userDetails);

        fcmService.sendMessage(user.getDeviceToken(), "test title", "test body");

        return ResponseEntity.ok().build();
    }


    @PostMapping("/save")
    @Operation(summary = "알림 저장 테스트")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
    public ResponseEntity makeAlert(@AuthenticationPrincipal UserDetails userDetails) {
        User user = securityService.getUserByUserDetails(userDetails);

        FcmRequestDto fcmRequestDto = new FcmRequestDto();

        alertService.saveAlert(user, fcmRequestDto);

        return ResponseEntity.ok().build();
    }


}