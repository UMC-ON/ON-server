package com.on.server.global.login.presentation;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KakaoApi {

    @Value("${kakao.api_key}")
    private String kakaoApiKey;

    @Value("${kakao.redirect_url}")
    private String kakaoRedirectUrl;

    private static String STATIC_KAKAO_API_KEY;
    private static String STATIC_KAKAO_REDIRECT_URL;

    @PostConstruct
    public void init() {
        STATIC_KAKAO_API_KEY = this.kakaoApiKey;
        STATIC_KAKAO_REDIRECT_URL = this.kakaoRedirectUrl;
    }

    public static String getKakaoApiKey() {
        return STATIC_KAKAO_API_KEY;
    }

    public static String getKakaoRedirectUrl() {
        return STATIC_KAKAO_REDIRECT_URL;
    }
}