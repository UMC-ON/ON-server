package com.on.server.global.login.presentation;

import com.on.server.domain.user.domain.User;
import com.on.server.global.login.application.JwtTokenProvider;
import com.on.server.global.login.application.KakaoService;
import com.on.server.global.login.application.RefreshTokenService;
import com.on.server.global.login.dto.KakaoUserInfoResponseDto;
import com.on.server.global.login.dto.TokenRefreshRequestDto;
import com.on.server.global.login.dto.TokenRefreshResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/kakao")
@Tag(name = "Kakao", description = "Kakao API")
public class KakaoController {

    private final KakaoService kakaoService;

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    //private final UserService userService;


    @GetMapping("/login/oauth2/code/kakao")
    public ResponseEntity<?> callbackKakao(@RequestParam("token") String token) {
        String accessToken = kakaoService.getAccessTokenFromKakao(token);

        KakaoUserInfoResponseDto kakaoUserInfoResponseDto = kakaoService.getUserInfo(accessToken);

        System.out.println("[[[[[user Email]]]]");
        System.out.println(kakaoUserInfoResponseDto.getKakaoAccount().getEmail());
        System.out.println("[[[[[user Email]]]]");

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequestDto request) {
        String refreshToken = request.getRefreshToken();
        if (refreshTokenService.validateToken(refreshToken)) {
            String userId = jwtTokenProvider.getUserIdFromJWT(refreshToken);
            Optional<User> optionalUser = userService.findByEmail(userId);
            if (optionalUser.isPresent()) {
                String newAccessToken = jwtTokenProvider.generateToken(userId);
                return ResponseEntity.ok(TokenRefreshResponseDto.builder()
                        .accessToken(newAccessToken)
                        .refreshToken(refreshToken)
                        .build());
            }
        }
        return ResponseEntity.status(403).body("Invalid refresh token");
    }

     */
}
