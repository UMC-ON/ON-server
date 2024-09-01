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
public class CompanyChatRoomListDto {
    private Integer roomCount; // 채팅방 수
    private List<CompanyRoomDto> roomList; // 채팅방 목록
}
