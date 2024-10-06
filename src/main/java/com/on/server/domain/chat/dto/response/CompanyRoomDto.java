package com.on.server.domain.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CompanyRoomDto {
    private Long roomId;
    private String senderName;
    private String location;
    private String lastMessage;
    private String lastChatTime;
}
