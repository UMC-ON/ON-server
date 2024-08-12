package com.on.server.domain.user.presentation;

import com.on.server.domain.user.application.UserService;
import com.on.server.domain.user.domain.User;
import com.on.server.domain.user.dto.SignInRequestDto;
import com.on.server.domain.user.dto.SignUpRequestDto;
import com.on.server.domain.user.dto.JwtToken;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Tag(name = "User", description = "User API")
public class UserController {

    private final UserService userService;

    // 로그인
    @PostMapping("/sign-in")
    public ResponseEntity<JwtToken> signIn(
            @RequestBody SignInRequestDto signInRequestDto
    ) {
        log.info("sign-in");
        String email = signInRequestDto.getEmail();
        String password = signInRequestDto.getPassword();
        JwtToken jwtToken = userService.signIn(email, password);
        log.info("request email = {}, password = {}", email, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());
        return ResponseEntity.ok(jwtToken);
    }

    // 회원가입
    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(
            @RequestBody SignUpRequestDto signUpRequestDto
    ) {
        userService.signUp(signUpRequestDto);
        return ResponseEntity.ok().build();
    }

    // 이메일 중복 체크
    @PostMapping("/duplicate_check/email")
    public ResponseEntity<Boolean> isDuplicateEmail(
            @RequestBody String email
    ) {
        return ResponseEntity.ok(userService.isDuplicateEmail(email));
    }

    // 닉네임 중복 체크
    @PostMapping("/duplicate_check/nickname")
    public ResponseEntity<Boolean> isDuplicateNickname(
            @RequestBody String nickname
    ) {
        return ResponseEntity.ok(userService.isDuplicateNickname(nickname));
    }


    @PostMapping("/test")
    @PreAuthorize("@securityService.isActiveAndNotNoneUser() and hasAnyRole('ACTIVE', 'AWAIT', 'TEMPORARY')")
    public ResponseEntity<User> test(
            @AuthenticationPrincipal UserDetails userDetails
            ) {
        User user = userService.getUserByUserDetails(userDetails);

        return ResponseEntity.ok(userService.test(user));
    }

}
