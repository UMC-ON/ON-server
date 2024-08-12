package com.on.server.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomListResponseDto {
    private int roomCount; // 채팅방 수
    private List<roomListDto> roomList; // 채팅방 목록

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class roomListDto {
        private Long roomId;
        private String senderName;
        private String profileImg;
        private String lastMessage;
        private String lastChatTime;
    }
}
