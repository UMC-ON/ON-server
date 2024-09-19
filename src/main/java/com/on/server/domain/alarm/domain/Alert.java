package com.on.server.domain.alarm.domain;

import com.on.server.domain.user.domain.User;
import com.on.server.global.common.ResponseCode;
import com.on.server.global.common.exceptions.InternalServerException;
import com.on.server.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Alert extends BaseEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "contents", nullable = false)
    private String contents;

    @Column(name = "alert_connect_id") // 게시글 알림일 경우만 추가하기 (게시글 아닐 경우 null 값 저장)
    private Long alertConnectId;

    @Enumerated(EnumType.STRING)
    @Column(name = "alert_type", nullable = false)
    private AlertType alertType;

    // 알림을 받는 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 알림 읽음 여부
    @Column(name = "is_read", nullable = false)
    private boolean isRead = false; // 기본값은 읽지 않음(false)

    public void updateIsRead(boolean isRead) {
        if (isRead == false) {
            isRead = true;
        }
        this.isRead = isRead;
    }

    @Builder
    private Alert(String title, String contents, Long alertConnectId, AlertType alertType, User user, boolean isRead) {
        this.title = title;
        this.contents = contents;
        this.alertConnectId = alertConnectId;
        this.alertType = alertType;
        this.user = user;
        this.isRead = isRead;
    }

}
