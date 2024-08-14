package com.on.server.domain.chat.dto;

import com.on.server.domain.chat.domain.ChatType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ChatRequestDto {
    private ChatType chatType;
    private Long receiverId;
}
