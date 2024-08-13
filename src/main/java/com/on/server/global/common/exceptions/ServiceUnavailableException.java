package com.on.server.global.common.exceptions;

import com.on.server.global.common.BaseRuntimeException;
import com.on.server.global.common.ResponseCode;

public class ServiceUnavailableException extends BaseRuntimeException {

    public ServiceUnavailableException(ResponseCode responseCode) {
        super(responseCode, responseCode.getMessage());
    }

    public ServiceUnavailableException(ResponseCode responseCode, String message) {
        super(responseCode, message);
    }

}
