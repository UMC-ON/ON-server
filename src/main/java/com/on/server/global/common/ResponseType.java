package com.on.server.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseType {

    SUCCESS(100),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    INTERNAL_SERVER_ERROR(500),
    SERVICE_UNAVAILABLE(503);

    private final Integer responseCode;

}
