package com.nanal.backend.global.throttling;

import com.nanal.backend.global.security.AuthenticationUtil;
import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Slf4j
@Component
public class ThrottlingInterceptor implements HandlerInterceptor {

    private final UserRateLimiter userRateLimiter;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userEmail = AuthenticationUtil.getCurrentUserEmail();
        Bucket bucket = userRateLimiter.resolveBucket(userEmail);

        long availableTokens = bucket.getAvailableTokens();
        log.info("[{}] remainToken : {}", userEmail, availableTokens);

        if (isEmptyBucket(bucket)) throw TooManyRequestException.EXCEPTION;

        return true;
    }

    private static boolean isEmptyBucket(Bucket bucket) {
        return !bucket.tryConsume(1);
    }
}