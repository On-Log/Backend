package com.nanal.backend.domain.token;

import com.nanal.backend.domain.mypage.repository.MemberRepository;
import com.nanal.backend.domain.oauth.UserDto;
import com.nanal.backend.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends GenericFilterBean { // GenericFilterBean => OncePer~~ 로 변경

    private final MemberRepository memberRepository;
    private final TokenService tokenService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = ((HttpServletRequest)request).getHeader("Token");

        if (token != null && tokenService.verifyToken(token)) {
            // 토큰 파싱해서 email 정보 가져오기
            String email = tokenService.getUid(token);

            // 토큰에 저장된 email 정보와 일치하는 사용자 정보를 DB 에서 가져오기
            Member existMember = memberRepository.findByEmail(email).orElseThrow(() -> new RuntimeException());

            // Authentication 정보 만들기
            UserDto userDto = UserDto.builder()
                    .email(existMember.getEmail())
                    .name(existMember.getName())
                    .build();

            // ContextHolder 에 Authentication 정보 저장
            Authentication auth = getAuthentication(userDto);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(request, response);
    }

    public Authentication getAuthentication(UserDto member) {
        return new UsernamePasswordAuthenticationToken(member, "",
                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
