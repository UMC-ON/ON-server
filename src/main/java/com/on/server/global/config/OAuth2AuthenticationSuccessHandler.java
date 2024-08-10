package com.on.server.global.config;

import com.on.server.global.login.application.RefreshTokenService;
import com.on.server.global.login.application.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

public class OAuth2AuthenticationSuccessHandler {

}

/*
@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    public OAuth2AuthenticationSuccessHandler(JwtTokenProvider tokenProvider, RefreshTokenService refreshTokenService) {
        this.tokenProvider = tokenProvider;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String userId = ((OAuth2User) authentication.getPrincipal()).getAttribute("id");
        String token = tokenProvider.generateToken(userId);
        String refreshToken = tokenProvider.generateRefreshToken(userId);

        refreshTokenService.saveRefreshToken(userId, refreshToken);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter writer = response.getWriter();
        writer.write("{\"token\":\"" + token + "\", \"refreshToken\":\"" + refreshToken + "\"}");
        writer.flush();
    }
}

 */
