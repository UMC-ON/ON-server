package com.on.server.global.login.exception;


import com.on.server.global.error.BaseException;

public class LoginException extends BaseException {

  public LoginException(final LoginExceptionType exceptionType) {
    super(exceptionType);
  }
}
