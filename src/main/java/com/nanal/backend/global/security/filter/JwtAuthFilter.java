package com.nanal.backend.global.security.filter;

import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.domain.auth.repository.MemberRepository;
import com.nanal.backend.global.security.AuthenticationUtil;
import com.nanal.backend.global.exception.customexception.TokenInvalidException;
import com.nanal.backend.global.security.jwt.TokenUtil;
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
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final MemberRepository memberRepository;
    private final String[] ignoredPaths = {"/login/**", "/auth/**", "/docs/**", "/favicon.ico", "/actuator/**", "/health", "/test/**"};
    private final TokenUtil tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // todo: 테스트용 로깅
        if(!request.getRequestURI().matches("/health")) log.info("[Request URL] " + request.getRequestURL());
        // filter skip
        for(String path : ignoredPaths){
            RequestMatcher ignoredPath = new AntPathRequestMatcher(path);
            if (ignoredPath.matches(request)) {
                chain.doFilter(request, response);
                return;
            }
        }

        String token = request.getHeader("Token");

        if (token != null && tokenService.verifyToken(token)) {
            try {
                // 토큰 파싱해서 socialId 정보 가져오기
                String socialId = tokenService.getSocialId(token);
                Member member = memberRepository.findBySocialId(socialId).get();

                // 이메일로 Authentication 정보 생성
                AuthenticationUtil.makeAuthentication(member);
            } catch (Exception e) {
                throw TokenInvalidException.EXCEPTION;
            }
        }else{
            // 여기서 예외를 발생시켜야 JwtExceptionFilter 로 떨어짐.
            throw TokenInvalidException.EXCEPTION;
        }

        chain.doFilter(request, response);
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
}
