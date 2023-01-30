package com.nanal.backend.global.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;

public class AuthenticationUtil {

    public static String getCurrentUserEmail() {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getEmail();
    }

    public static Authentication getAuthentication(User user) {
        return new UsernamePasswordAuthenticationToken(user, "",
                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
    }

    public static void makeAuthentication(String socialId, String email) {
        // Authentication 정보 만들기
        User user = User.builder()
                .socialId(socialId)
                .email(email)
                .build();

        // ContextHolder 에 Authentication 정보 저장
        Authentication auth = AuthenticationUtil.getAuthentication(user);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}

