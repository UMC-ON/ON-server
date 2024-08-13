package com.on.server.global.security;

import com.on.server.domain.user.domain.User;
import com.on.server.domain.user.domain.repository.UserRepository;
import com.on.server.global.common.ResponseCode;
import com.on.server.global.common.exceptions.InternalServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;

    public boolean isActiveAndNotNoneUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        User user = (User) authentication.getPrincipal();
        return user.getRoles().stream()
                        .noneMatch(authority -> authority.equals("TEMPORARY"));
    }

    public User getUserByUserDetails(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new InternalServerException(ResponseCode.INTERNAL_SERVER, "Token에 해당하는 사용자 정보를 찾을 수 없습니다. 관리자에게 문의 바랍니다."));
    }
}
