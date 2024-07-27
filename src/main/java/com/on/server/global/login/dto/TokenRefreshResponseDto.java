package com.on.server.global.login.dto;

import lombok.Builder;

@Builder
public class TokenRefreshResponseDto {

    private String accessToken;

    private String refreshToken;

}
