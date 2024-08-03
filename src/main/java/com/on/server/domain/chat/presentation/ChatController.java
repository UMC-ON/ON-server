package com.on.server.domain.chat.presentation;

import com.on.server.domain.chat.application.ChatService;
import com.on.server.global.common.CommonResponse;
import com.on.server.global.common.CustomException;
import com.on.server.global.common.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    // ** 추후 삭제 예정 **
    @GetMapping("/test")
    public CommonResponse<?> apiTest() {
        try {
            return new CommonResponse<>(ResponseCode.SUCCESS, chatService.getApiTest());
        } catch (CustomException e) {
            return new CommonResponse<>(e.getStatus());
        }
    }
    // ******************
}
