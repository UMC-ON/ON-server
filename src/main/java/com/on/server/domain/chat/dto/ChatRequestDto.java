package com.on.server.domain.chat.dto;

import com.on.server.domain.chat.domain.ChatType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRequestDto {
    private ChatType chatType;
    private Long receiverId;
}
