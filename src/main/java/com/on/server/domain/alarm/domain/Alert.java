package com.on.server.domain.alarm.domain;

import com.on.server.domain.user.domain.User;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alert_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    private String contents;

    @Column(name = "alert_connect_id") // 게시글 알림일 경우만 추가하기 (게시글 아닐 경우 null 값 저장)
    private Long alertConnectId;

//    @Column(name = "redirect_url")
//    private String redirectUrl;

    @Enumerated(EnumType.STRING)
    private AlertType alertType;

    // 알림을 받는 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    private Alert(String title, String contents, Long alertConnectId, AlertType alertType, User user) {
        this.title = title;
        this.contents = contents;
        this.alertConnectId = alertConnectId;
        this.alertType = alertType;
        this.user = user;
    }

}
