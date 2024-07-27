package com.on.server.global.error;

import org.springframework.http.HttpStatus;

public interface BaseExceptionType {

    HttpStatus getHttpStatus();

    String getMessage();

}
