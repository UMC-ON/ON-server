package com.on.server.domain.user.application;

import com.on.server.domain.user.domain.User;
import com.on.server.domain.user.domain.UserStatus;
import com.on.server.domain.user.domain.repository.UserRepository;
import com.on.server.domain.user.dto.request.SignUpRequestDto;
import com.on.server.domain.user.dto.response.UserInfoResponseDto;
import com.on.server.global.common.ResponseCode;
import com.on.server.global.common.exceptions.BadRequestException;
import com.on.server.global.common.exceptions.InternalServerException;
import com.on.server.global.jwt.JwtTokenProvider;
import com.on.server.domain.user.dto.request.JwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    // private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public JwtToken signIn(String email, String password) {
        // 1. username + password 를 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        // authenticationManagerBuilder -> authenticationManager 대체 사용(버전 문제)
        // Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        return jwtToken;
    }

    @Transactional
    public void signUp(SignUpRequestDto signUpRequestDto) {
        if (userRepository.existsByEmail(signUpRequestDto.getEmail())) {
            throw new BadRequestException(ResponseCode.ROW_ALREADY_EXIST, "이미 사용 중인 사용자 이메일입니다.");
        }

        userRepository.save(signUpRequestDto.toEntity(
                passwordEncoder.encode(signUpRequestDto.getPassword()),
                UserStatus.TEMPORARY
        ));
    }

    public UserStatus getUserStatusByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new BadRequestException(ResponseCode.ROW_DOES_NOT_EXIST, "해당하는 회원을 찾을 수 없습니다."));
        Set<UserStatus> userStatusSet = user.getRoles();
        if (userStatusSet.size() != 1)
            throw new InternalServerException(ResponseCode.INTERNAL_SERVER, "User 권한은 하나여야 합니다. 관리자에게 문의 바랍니다. 현재 권한 수: " + userStatusSet.size());
        return userStatusSet.stream().findFirst().get();
    }

    public UserInfoResponseDto getUserInfoByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
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
        return userRepository.existsByEmail(email);
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

}
