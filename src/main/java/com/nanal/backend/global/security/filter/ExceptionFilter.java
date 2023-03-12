package com.nanal.backend.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nanal.backend.global.exception.customexception.TokenInvalidException;
import com.nanal.backend.global.response.CommonResponse;
import com.nanal.backend.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // JwtFilter 를 호출하는데, 이 필터에서 TokenInvalidException 이 떨어진다.
            filterChain.doFilter(request, response);
        } catch (TokenInvalidException e) {
            log.info("[{}] [{}] {}",
                    e.getClass().getSimpleName(),
                    extractClientIP(request),
                    e.getErrorCode().getMessage());

            setErrorResponse(response, ErrorCode.INVALID_TOKEN);
        }
    }

    private String extractClientIP(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Real-IP");
        if (clientIp == null) {
            clientIp = request.getHeader("X-Forwarded-For");
        }
        if (clientIp == null) {
            clientIp = request.getRemoteAddr();
        }
        return clientIp;
    }

    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getCode());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), new CommonResponse<>(errorCode));
    }
}