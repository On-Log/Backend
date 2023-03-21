package com.nanal.backend.global.config;

import com.nanal.backend.global.throttling.ThrottlingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 현재 빈 등록 x
 */
@RequiredArgsConstructor
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    private final ThrottlingInterceptor throttlingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(throttlingInterceptor)
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/auth/**", "/docs/**", "/favicon.ico", "/error", "/health", "/test/**");
    }
}
