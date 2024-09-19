package com.on.server.domain.user.presentation;

import com.on.server.domain.user.application.UserService;
import com.on.server.domain.user.domain.User;
import com.on.server.domain.user.domain.UserStatus;
import com.on.server.domain.user.dto.request.*;
import com.on.server.domain.user.dto.response.UserInfoResponseDto;
import com.on.server.global.security.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.URL;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Tag(name = "사용자", description = "User API, 사용자 관련 API")
public class UserController {

    private final SecurityService securityService;

    private final UserService userService;

    // 로그인
    @Operation(summary = "로그인")
    @PostMapping("/sign-in")
    public ResponseEntity<JwtToken> signIn(
            @RequestBody SignInRequestDto signInRequestDto
    ) {
        log.info("sign-in");

        String loginId = signInRequestDto.getLoginId();
        String password = signInRequestDto.getPassword();
        JwtToken jwtToken = userService.signIn(loginId, password);

        log.info("request loginId = {}, password = {}", loginId, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());

        return ResponseEntity.ok(jwtToken);
    }

    // 토큰 갱신
    @Operation(summary = "토큰 갱신")
    @PostMapping("/update-token")
    public ResponseEntity<JwtToken> updateToken(
            @RequestBody @NotBlank String refreshToken
    ) {
        return ResponseEntity.ok(userService.updateToken(refreshToken));
    }

    // 로그아웃
    @Operation(summary = "로그아웃")
    @PostMapping("/sign-out")
    public ResponseEntity<Void> signOut(
            @RequestBody @Valid SignOutRequestDto signoutRequestDto
    ) {
        userService.signOut(signoutRequestDto);
        return ResponseEntity.ok().build();
    }

    // 회원가입
    @Operation(summary = "회원가입")
    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(
            @RequestBody SignUpRequestDto signUpRequestDto
    ) {
        userService.signUp(signUpRequestDto);
        return ResponseEntity.ok().build();
    }

    // 이메일 중복 체크
    @Operation(summary = "이메일 중복 체크")
    @PostMapping("/duplicate_check/email")
    public ResponseEntity<Boolean> isDuplicateEmail(
            @RequestBody String email
    ) {
        return ResponseEntity.ok(userService.isDuplicateEmail(email));
    }

    // 닉네임 중복 체크
    @Operation(summary = "닉네임 중복 체크")
    @PostMapping("/duplicate_check/nickname")
    public ResponseEntity<Boolean> isDuplicateNickname(
            @RequestBody String nickname
    ) {
        return ResponseEntity.ok(userService.isDuplicateNickname(nickname));
    }

    // 현재 사용자 UserStatus 조회
    @Operation(summary = "현재 사용자 UserStatus 조회")
    @GetMapping("/current/status")
    public ResponseEntity<UserStatus> getCurrentUserStatus(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(userService.getUserStatusByEmail(userDetails.getUsername()));
    }

    // 현재 사용자 정보 조회
    @Operation(summary = "현재 사용자 정보 조회")
    @GetMapping("/current/info")
    public ResponseEntity<UserInfoResponseDto> getCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(userService.getUserInfoByLoginId(userDetails.getUsername()));
    }

    // 현재 사용자 닉네임 수정
    @Operation(summary = "현재 사용자 닉네임 수정")
    @PutMapping("/current/update/nickname")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    public ResponseEntity<Void> updateCurrentUserNickname(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody String nickname
    ) {
        userService.updateUserNickName(securityService.getUserByUserDetails(userDetails), nickname);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "현재 사용자 교환/방문교 URL 수정")
    @PutMapping("/current/update/univ_url")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    public ResponseEntity<Void> updateCurrentUserUniversityUrl(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @URL(message = "올바른 URL 형식이 아닙니다.") String universityUrl
    ) {
        userService.updateUserUniversityUrl(securityService.getUserByUserDetails(userDetails), universityUrl);
        return ResponseEntity.ok().build();
    }

    // 회원 탈퇴
    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/delete")
    @PreAuthorize("@securityService.isNotTemporaryUser()")
    public ResponseEntity<Void> deleteUser(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        userService.deleteUser(securityService.getUserByUserDetails(userDetails));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "ID 찾기")
    @PostMapping("/find-id")
    public ResponseEntity<String> findId(
            @RequestBody FindIDRequestDto findIDRequestDto
    ) {
        return ResponseEntity.ok(userService.findId(findIDRequestDto));
    }

    @Operation(summary = "비밀번호 찾기")
    @PostMapping("/find-password")
    public ResponseEntity<String> findPassword(
            @RequestBody FindPWRequestDto findPWRequestDto
    ) {
        return ResponseEntity.ok(userService.findPassword(findPWRequestDto));
    }


    /** 로그인 테스트 API
     * PreAuthorize: 해당 메소드에 대한 접근 권한을 설정
     * @securityService.isNotTemporaryUser(): 임시 사용자가 아닌 경우 필터링
     * hasAnyRole('ADMIN', 'ACTIVE', 'AWAIT', 'DENIED', 'NON_CERTIFIED'): ADMIN, ACTIVE, AWAIT, DENIED, NON_CERTIFIED 권한 중 하나라도 가지고 있는 경우 필터링
     */
    @Operation(summary = "로그인 테스트 API, test 용도")
    @PostMapping("/test")
    //
    @PreAuthorize("@securityService.isNotTemporaryUser() or hasAnyRole('ADMIN', 'ACTIVE', 'AWAIT', 'DENIED', 'NON_CERTIFIED')")
    public ResponseEntity<User> test(
            @AuthenticationPrincipal UserDetails userDetails
            ) {
        User user = securityService.getUserByUserDetails(userDetails);

        return ResponseEntity.ok(userService.test(user));
    }

}
