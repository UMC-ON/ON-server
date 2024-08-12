package com.on.server.global.common.exceptions;

import com.on.server.global.common.ResponseCode;

public class InternalServerException extends BaseRuntimeException {

    public InternalServerException(ResponseCode responseCode) {
        super(responseCode, responseCode.getMessage());
    }

    public InternalServerException(ResponseCode responseCode, String message) {
        super(responseCode, message);
    }

}
