package com.nanal.backend.global.security.filter;

import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.domain.auth.repository.MemberRepository;
import com.nanal.backend.global.security.AuthenticationUtil;
import com.nanal.backend.global.exception.customexception.TokenInvalidException;
import com.nanal.backend.global.security.jwt.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final MemberRepository memberRepository;
    private final String[] ignoredPaths = {"/main", "/login/**", "/auth/**", "/docs/**", "/favicon.ico"};

    private final TokenUtil tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        log.info("[Request URL] {}", request.getRequestURL());
        // "/auth/**" url 로 요청시, 해당 필터 스킵.
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
                AuthenticationUtil.makeAuthentication(socialId, member.getEmail(), Arrays.asList(member.getRole().getKey()));
            } catch (Exception e) {
                throw TokenInvalidException.EXCEPTION;
            }
        }else{
            // 여기서 예외를 발생시켜야 JwtExceptionFilter 로 떨어짐.
            log.info("예외 발생 url = {}", request.getRequestURL());
            throw TokenInvalidException.EXCEPTION;
        }

        chain.doFilter(request, response);
    }
}
