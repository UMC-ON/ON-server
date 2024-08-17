package com.on.server.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ChatListResponseDto {
    private Long currentUserId;
    private List<chatListDto> chatList;

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    public static class chatListDto {
        private String message; // 채팅 메시지 내용
        private Long userId; // 채팅 작성자 ID
    }
}