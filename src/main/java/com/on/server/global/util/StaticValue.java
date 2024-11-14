package com.on.server.global.util;

public class StaticValue {

    public final static Long JWT_ACCESS_TOKEN_VALID_TIME = 1000L * 60 * 60; // 1시간
    public final static Long JWT_REFRESH_TOKEN_VALID_TIME = 1000L * 60 * 60 * 24 * 14; // 2주

    public final static Long SIGN_UP_AUTH_NUMBER_VALID_TIME = 1000L * 60 * 5; // 5분
    public final static Long FIND_PASSWORD_AUTH_NUMBER_VALID_TIME = 1000L * 60 * 5; // 5분

    public final static String AUTH_NUMBER_MAIL_SUBJECT = "On: 이메일 인증";
    public final static String AUTH_NUMBER_MAIL_BODY = "<h3>On: 이용에 감사드립니다.</h3><h3>아래 인증번호를 서비스에 입력해주시기 바랍니다.</h3><h3>감사합니다.</h3>";

}
