package com.on.server.global.redis;


import com.on.server.global.util.StaticValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisUtils {

    private final RedisTemplate<String, Object> redisTemplate;

    /* 로그인 관련 Redis 메서드
     * 1. RefreshToken
     * Key: RefreshToken:{RefreshToken 값}
     * Value: {User ID}
     *
     * 2. Blacklist
     * Key: Blacklist:{AccessToken 값}
     * Value: "BLACKLISTED"
     */
    public void setRefreshTokenData(String key, Long userId, Long expiredTime){
        String redisKey = "RefreshToken:" + key;
        redisTemplate.opsForValue().set(redisKey, userId, expiredTime, TimeUnit.MILLISECONDS);
    }

    public Long getRefreshTokenData(String key){
        String redisKey = "RefreshToken:" + key;

        Object value = redisTemplate.opsForValue().get(redisKey);
        if (value == null) {
            return null;
        }

        if (value.getClass().equals(Long.class)) {
            return (Long) value;
        } else if (value.getClass().equals(Integer.class)) {
            return ((Integer) value).longValue();
        } else if (value.getClass().equals(String.class)) {
            return Long.parseLong((String) value);
        } else {
            return null;
        }
    }

    public void deleteRefreshTokenData(String key){
        String redisKey = "RefreshToken:" + key;
        redisTemplate.delete(redisKey);
    }

    public void addToBlacklist(String token) {
        String redisKey = "Blacklist:" + token;
        redisTemplate.opsForValue().set(redisKey, "BLACKLISTED", StaticValue.JWT_ACCESS_TOKEN_VALID_TIME, TimeUnit.SECONDS);
    }

    public boolean isTokenBlacklisted(String token) {
        String redisKey = "Blacklist:" + token;
        return "BLACKLISTED".equals(redisTemplate.opsForValue().get(redisKey));
    }

}
