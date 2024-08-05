package com.on.server.domain.alarm.dto;

import com.on.server.domain.alarm.domain.AlertType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FcmRequestDto {
    // 알림 받는 유저 디바이스 토큰
    private String targetToken;

    // 알림 제목
    private String title;

    // 알림 내용
    private String body;

    // 알림 타입
    private AlertType alertType;

}
