package com.on.server.global.common.exceptions;

import com.on.server.global.common.BaseRuntimeException;
import com.on.server.global.common.ResponseCode;

public class BadRequestException extends BaseRuntimeException {

    public BadRequestException(ResponseCode responseCode) {
        super(responseCode, responseCode.getMessage());
    }

    public BadRequestException(ResponseCode responseCode, String message) {
        super(responseCode, message);
    }

}
