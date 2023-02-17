package com.nanal.backend.global.security;

import com.nanal.backend.domain.auth.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@RequiredArgsConstructor
@Component
public class AuthenticationUtil {

    public static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String getCurrentUserEmail() {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getEmail();
    }

    public static Authentication getAuthentication(User user) {
        return new UsernamePasswordAuthenticationToken(user, "",
                Arrays.asList(new SimpleGrantedAuthority(user.getRole())));
    }

    public static void makeAuthentication(String socialId, String email, String role) {
        // Authentication 정보 만들기
        User user = User.builder()
                .socialId(socialId)
                .email(email)
                .role(role)
                .build();

        // ContextHolder 에 Authentication 정보 저장
        Authentication auth = AuthenticationUtil.getAuthentication(user);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}

