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
public class CompanyChatRoomListResponseDto {
    private Integer roomCount; // 채팅방 수
    private List<roomListDto> roomList; // 채팅방 목록

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    public static class roomListDto {
        private Long roomId;
        private String senderName;
        private String location;
        private String lastMessage;
        private String lastChatTime;
    }
}
