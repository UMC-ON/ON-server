package com.on.server.global.login.presentation;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KakaoApi {

    @Value("${kakao.api_key}")
    public static String kakaoApiKey;

    @Value("${kakao.redirect_url}")
    public static String kakaoRedirectUrl;

    public static String getKakaoApiKey() {
        return kakaoApiKey;
    }

    public static String getKakaoRedirectUrl() {
        return kakaoRedirectUrl;
    }
}
