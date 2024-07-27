package com.on.server.domain.user.presentation;

import com.on.server.global.login.application.KakaoService;
import com.on.server.domain.user.dto.KakaoUserInfoResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/kakao")
@Tag(name = "Kakao", description = "Kakao API")
public class KakaoController {

    private final KakaoService kakaoService;

    @GetMapping("/login/oauth2/code/kakao")
    public ResponseEntity<?> callbackKakao(@RequestParam("code") String code) throws IOException {
        String accessToken = kakaoService.getAccessTokenFromKakao(code);

        KakaoUserInfoResponseDto kakaoUserInfoResponseDto = kakaoService.getUserInfo(accessToken);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
