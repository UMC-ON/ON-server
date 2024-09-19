package com.on.server.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignOutRequestDto {

    @NotBlank
    private String accessToken;

    @NotBlank
    private String refreshToken;

}
