package com.on.server.global.common.exceptions;

import com.on.server.global.common.BaseRuntimeException;
import com.on.server.global.common.ResponseCode;

public class UnauthorizedException extends BaseRuntimeException {

    public UnauthorizedException(ResponseCode responseCode) {
        super(responseCode, responseCode.getMessage());
    }

    public UnauthorizedException(ResponseCode responseCode, String message) {
        super(responseCode, message);
    }

}
