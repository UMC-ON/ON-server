package com.on.server.domain.user.application;

import com.on.server.domain.user.domain.User;
import com.on.server.domain.user.domain.UserStatus;
import com.on.server.domain.user.domain.repository.UserRepository;
import com.on.server.domain.user.dto.request.SignUpRequestDto;
import com.on.server.domain.user.dto.request.SignOutRequestDto;
import com.on.server.domain.user.dto.response.UserInfoResponseDto;
import com.on.server.global.common.ResponseCode;
import com.on.server.global.common.exceptions.BadRequestException;
import com.on.server.global.common.exceptions.InternalServerException;
import com.on.server.global.jwt.JwtTokenProvider;
import com.on.server.domain.user.dto.request.JwtToken;
import com.on.server.global.redis.RedisUtils;
import com.on.server.global.util.StaticValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    private final RedisUtils redisUtils;

    @Transactional
    public JwtToken signIn(String loginId, String password) {

        User user = userRepository.findByLoginId(loginId).orElseThrow(
                () -> new BadRequestException(ResponseCode.ROW_DOES_NOT_EXIST, "해당하는 회원을 찾을 수 없습니다."));

        // 1. username + password 를 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginId, password);

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        // authenticationManagerBuilder -> authenticationManager 대체 사용(버전 문제)
        // Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        redisUtils.setRefreshTokenData(jwtToken.getRefreshToken(), user.getId(), StaticValue.JWT_REFRESH_TOKEN_VALID_TIME);

        return jwtToken;
    }

    @Transactional
    public void signUp(SignUpRequestDto signUpRequestDto) {
        if (userRepository.existsByLoginId(signUpRequestDto.getLoginId())) {
            throw new BadRequestException(ResponseCode.ROW_ALREADY_EXIST, "이미 사용 중인 사용자 이메일입니다.");
        }

        userRepository.save(signUpRequestDto.toEntity(
                passwordEncoder.encode(signUpRequestDto.getPassword()),
                UserStatus.TEMPORARY
        ));
    }

    public JwtToken updateToken(String refreshToken) {
        // STEP 1: Redis에서 refreshToken으로 사용자 ID 조회
        // STEP 2: 해당 사용자 정보를 가져오기
        User user = userRepository.findById(getUserIdFromRefreshToken(refreshToken)).orElseThrow(
                () -> new BadRequestException(ResponseCode.ROW_DOES_NOT_EXIST, "해당하는 사용자 정보를 찾을 수 없습니다.")
        );

        // STEP 4: 인증 정보 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getLoginId(), null, user.getAuthorities());

        // STEP 5: 새 JWT 토큰(액세스, 리프레시 토큰 모두) 생성
        JwtToken newJwtToken = jwtTokenProvider.generateToken(authentication);

        // STEP 6: 새로운 리프레시 토큰을 Redis에 저장 (기존 것 대체)
        redisUtils.setRefreshTokenData(newJwtToken.getRefreshToken(), user.getId(), StaticValue.JWT_REFRESH_TOKEN_VALID_TIME);

        redisUtils.deleteRefreshTokenData(refreshToken);

        return newJwtToken;
    }

    public void signOut(SignOutRequestDto signoutRequestDto) {
        if (redisUtils.isTokenBlacklisted(signoutRequestDto.getAccessToken())) {
            throw new BadRequestException(ResponseCode.INVALID_PARAMETER, "이미 로그아웃된 사용자입니다.");
        }
        redisUtils.addToBlacklist(signoutRequestDto.getAccessToken());
        redisUtils.deleteRefreshTokenData(signoutRequestDto.getRefreshToken());
    }

    public UserStatus getUserStatusByEmail(String loginId) {
        User user = userRepository.findByLoginId(loginId).orElseThrow(
                () -> new BadRequestException(ResponseCode.ROW_DOES_NOT_EXIST, "해당하는 회원을 찾을 수 없습니다."));
        Set<UserStatus> userStatusSet = user.getRoles();
        if (userStatusSet.size() != 1)
            throw new InternalServerException(ResponseCode.INTERNAL_SERVER, "User 권한은 하나여야 합니다. 관리자에게 문의 바랍니다. 현재 권한 수: " + userStatusSet.size());
        return userStatusSet.stream().findFirst().get();
    }

    public UserInfoResponseDto getUserInfoByLoginId(String loginID) {
        User user = userRepository.findByLoginId(loginID).orElseThrow(
                () -> new BadRequestException(ResponseCode.ROW_DOES_NOT_EXIST, "해당하는 회원을 찾을 수 없습니다."));
        return UserInfoResponseDto.from(user);
    }

    public void updateUserNickName(User user, String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new BadRequestException(ResponseCode.INVALID_PARAMETER, "이미 사용 중인 사용자 닉네임입니다.");
        }
        user.setNickname(nickname);
        userRepository.save(user);
    }

    public Boolean isDuplicateEmail(String email) {
        return userRepository.existsByLoginId(email);
    }

    public Boolean isDuplicateNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public User test(User user) {
        return userRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("해당하는 회원을 찾을 수 없습니다."));
    }

    public void updateUserUniversityUrl(User user, String universityUrl) {
        user.setUniversityUrl(universityUrl);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(User user) {
        user = user.deleteUser();
        userRepository.save(user);
    }

    private Long getUserIdFromRefreshToken(String refreshToken) {
        return Optional.ofNullable(redisUtils.getRefreshTokenData(refreshToken))
                .orElseThrow(() -> new BadRequestException(
                        ResponseCode.ROW_DOES_NOT_EXIST,
                        "해당하는 refreshToken에 맵핑된 사용자가 없습니다."
                ));
    }

}
