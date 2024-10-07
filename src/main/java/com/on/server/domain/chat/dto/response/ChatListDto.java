package com.on.server.domain.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ChatListDto {
    private Long chatUserOne; // 채팅방 생성한 사람
    private Long chatUserTwo;
    private List<ChatMessageDto> chatList;
}
