package com.on.server.domain.alarm.dto;

import com.on.server.domain.alarm.domain.AlertType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlertListResponseDto {
    private String title;

    private String content;

    private AlertType alertType;

    private Long alertConnectId; // 알림 사용 시 게시글 id, 채팅방 id 등
}
