package com.on.server.domain.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AlertUrlDto {

    private Long connectId;

    private String apiUrl;

}
