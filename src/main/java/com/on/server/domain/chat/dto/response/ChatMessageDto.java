package com.on.server.domain.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ChatMessageDto {
    private String message; // 채팅 메시지 내용
    private Long userId; // 채팅 작성자 ID
    private LocalDateTime createdAt; // 메시지 전송 시간
}