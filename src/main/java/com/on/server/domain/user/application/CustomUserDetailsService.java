package com.on.server.domain.user.application;


import com.on.server.domain.user.domain.repository.UserRepository;

import com.on.server.global.common.ResponseCode;
import com.on.server.global.common.exceptions.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByLoginId(email)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UnauthorizedException(ResponseCode.INVALID_SIGNIN));
    }

    // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 return
    private UserDetails createUserDetails(com.on.server.domain.user.domain.User user) {
        return User.builder()
                .username(user.getLoginId())
                //.password(passwordEncoder.encode(user.getPassword()))
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .build();
    }

}
