package com.on.server.global.security;

import com.on.server.domain.user.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    public boolean isActiveAndNotNoneUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        User user = (User) authentication.getPrincipal();
        return user.getRoles().stream()
                        .noneMatch(authority -> authority.equals("DENIED"));
    }
}
