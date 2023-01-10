package com.nanal.backend.global.interceptor;

import com.nanal.backend.domain.auth.repository.MemberRepository;
import com.nanal.backend.global.exception.customexception.MemberAuthException;
import com.nanal.backend.global.security.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final MemberRepository memberRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String socialId = user.getSocialId();

        // socialId 를 갖는 사용자 존재 여부
        memberRepository.findBySocialId(socialId).orElseThrow(() -> new MemberAuthException("존재하지 않는 유저입니다."));
        return true;
    }
}

