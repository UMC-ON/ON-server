package com.on.server.domain.alarm.dto;

import com.on.server.domain.alarm.domain.AlertType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FcmRequestDto { //알림 저장하기 위한 dto

    // 알림 제목
    private String title;

    // 알림 내용
    private String body;

    // 알림 타입
    private AlertType alertType;

    // 알림 사용 시 이동할 게시글 id (게시글 알림일 경우에만, 아닌 경우 null로)
    private Long alertConnectId;

}
