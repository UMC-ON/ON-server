package com.on.server.global.login.exception;

import capstone.safeat.base.BaseException;

public class LoginException extends BaseException {

  public LoginException(final LoginExceptionType exceptionType) {
    super(exceptionType);
  }
}
