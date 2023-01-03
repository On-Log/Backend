package com.nanal.backend.global.security.jwt;

import com.nanal.backend.global.security.AuthenticationUtil;
import com.nanal.backend.global.exception.customexception.TokenInvalidException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final String[] ignoredPaths = {"/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/analysis/**", "/actuator/**"};

    private final TokenUtil tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // "/auth/**" url 로 요청시, 해당 필터 스킵.
        for(String path : ignoredPaths){
            RequestMatcher ignoredPath = new AntPathRequestMatcher(path);
            if (ignoredPath.matches(request)) {
                chain.doFilter(request, response);
                return;
            }
        }

        String token = ((HttpServletRequest)request).getHeader("Token");

        if (token != null && tokenService.verifyToken(token)) {
            // 토큰 파싱해서 email 정보 가져오기
            String email = tokenService.getUid(token);

            // 이메일로 Authentication 정보 생성
            AuthenticationUtil.makeAuthentication(email);
        }else{
            // 여기서 예외를 발생시켜야 JwtExceptionFilter 로 떨어짐.
            throw new TokenInvalidException("Token 이 유효하지 않습니다.");
        }

        chain.doFilter(request, response);
    }
}
