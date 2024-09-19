package com.on.server.global.common;

import lombok.*;

@Getter
@Builder(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponseDto {

    private Integer responseCode;
    private String message;

    public static ErrorResponseDto of(ResponseCode responseCode, String message) {
        return ErrorResponseDto.builder()
                .responseCode(responseCode.getCode())
                .message(message)
                .build();
    }

    public static ErrorResponseDto of(ResponseCode responseCode) {
        return ErrorResponseDto.builder()
                .responseCode(responseCode.getCode())
                .message(responseCode.getMessage())
                .build();
    }

}
