package com.on.server.global.login.dto;

import lombok.Getter;

@Getter
public class LoginRequest {

  private final String code;

  private LoginRequest() {
    this(null);
  }

  public LoginRequest(final String code) {
    this.code = code;
  }
}
