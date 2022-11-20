package com.nanal.backend.global.auth.token;

import com.nanal.backend.domain.mypage.repository.MemberRepository;
import com.nanal.backend.global.auth.UserDto;
import com.nanal.backend.global.exception.customexception.TokenInvalidException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final RequestMatcher ignoredPaths = new AntPathRequestMatcher("/auth/**");

    private final TokenUtil tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // "/auth/**" url 로 요청시, 해당 필터 스킵.
        if (this.ignoredPaths.matches(request)) {
            chain.doFilter(request, response);
            return;
        }

        String token = ((HttpServletRequest)request).getHeader("Token");

        if (token != null && tokenService.verifyToken(token)) {
            // 토큰 파싱해서 email 정보 가져오기
            String email = tokenService.getUid(token);

            // Authentication 정보 만들기
            UserDto userDto = UserDto.builder()
                    .email(email)
                    //.name(existMember.getName())
                    .build();

            // ContextHolder 에 Authentication 정보 저장
            Authentication auth = getAuthentication(userDto);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }else{
            // 여기서 예외를 발생시켜야 JwtExceptionFilter 로 떨어짐.
            throw new TokenInvalidException("Token 이 유효하지 않습니다.");
        }

        chain.doFilter(request, response);
    }

    public Authentication getAuthentication(UserDto member) {
        return new UsernamePasswordAuthenticationToken(member, "",
                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
