package com.on.server.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class BaseRuntimeException extends RuntimeException {

    public final ResponseCode responseCode;
    public final String message;

}
