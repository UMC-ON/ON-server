package com.on.server.domain.chat.presentation;

import com.on.server.domain.chat.application.ChatService;
import com.on.server.domain.chat.application.ChatTestService;
import com.on.server.domain.user.domain.User;
import com.on.server.global.common.CommonResponse;
import com.on.server.global.common.ResponseCode;
import com.on.server.global.security.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatTest")
@Tag(name = "Chat", description = "Chat API")
public class ChatTestController {

    private final ChatService chatService;
    private final ChatTestService chatTestService;
    private final SecurityService securityService;

    // 모집 완료
//    @PostMapping("/{roomId}/recruit")
//    @Operation(summary = "모집 완료 시 버튼 눌렀을 때")
//    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
//    public CommonResponse<?> recruit(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("roomId") Long roomId) {
//
//        User user = securityService.getUserByUserDetails(userDetails);
//        chatTestService.completeRecruit(user, roomId);
//
//        return new CommonResponse<>(ResponseCode.SUCCESS);
//    }
}
