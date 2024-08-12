package com.on.server.domain.user.application;

import com.on.server.domain.user.domain.User;
import com.on.server.domain.user.domain.repository.UserRepository;
import com.on.server.domain.user.dto.SignUpDto;
import com.on.server.global.jwt.JwtTokenProvider;
import com.on.server.domain.user.dto.JwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    public Void signUp(SignUpDto signUpDto) {
        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 사용자 이름입니다.");
        }
        // Password 암호화
        String encodedPassword = passwordEncoder.encode(signUpDto.getPassword());
        List<String> roles = new ArrayList<>();
        roles.add("AWAIT");  // USER 권한 부여
        userRepository.save(signUpDto.toEntity(encodedPassword, roles));
        return null;
    }

    public User getUserByUserDetails(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new IllegalArgumentException("해당하는 회원을 찾을 수 없습니다."));
    }

    public User test(User user) {
        return userRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("해당하는 회원을 찾을 수 없습니다."));
    }

}
