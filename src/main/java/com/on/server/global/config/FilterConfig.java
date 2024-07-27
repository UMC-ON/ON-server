package com.on.server.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<LogFilter> myFilterRegistration() {
        final FilterRegistrationBean<LogFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new LogFilter());
        // 모든 URL에 필터 적용
        registration.addUrlPatterns(
                "/filter/*", "/members/*", "/login/*", "/categories/*", "/menu/*", "/groups/*"
        );
        registration.setOrder(1); // 필터 순서 지정 (optional)
        return registration;
    }

}
