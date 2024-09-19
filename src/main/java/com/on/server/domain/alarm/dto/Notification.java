package com.on.server.domain.alarm.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Notification {
    private String title;
    private String body;
    private String image;

    public static Notification toEntity(String title, String body) {
        return Notification.builder()
                .title(title)
                .body(body)
                .image(null)
                .build();
    }
}
