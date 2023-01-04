package com.nanal.backend.global.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserDto {
    private String socialId;
    private String email;
    private String name;
    private String provider;

    public static UserDto toDto(OAuth2User oAuth2User) {
        var attributes = oAuth2User.getAttributes();
        return UserDto.builder()
                .email((String)attributes.get("email"))
                .name((String)attributes.get("name"))
                .provider((String)attributes.get("provider"))
                .build();
    }
}
