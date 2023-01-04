package com.nanal.backend.global.security;

import com.nanal.backend.domain.auth.entity.Member;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;

public class AuthenticationUtil {

    public static String getCurrentUserEmail() {
        UserDto userDto = (UserDto)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDto.getEmail();
    }

    public static Authentication getAuthentication(UserDto member) {
        return new UsernamePasswordAuthenticationToken(member, "",
                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
    }

    public static void makeAuthentication(String socialId, String email) {
        // Authentication 정보 만들기
        UserDto userDto = UserDto.builder()
                .socialId(socialId)
                .email(email)
                //.name(existMember.getName())
                .build();

        // ContextHolder 에 Authentication 정보 저장
        Authentication auth = AuthenticationUtil.getAuthentication(userDto);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}

