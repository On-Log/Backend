package com.nanal.backend.domain.oauth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserDto {
    private String email;
    private String name;
    private String provider;

    /*public void update(String nickname) { //member내에서는 닉네임의 update만 발생.
        System.out.println("-------userDto 내 update확인: "+nickname+" | this값 확인: "+this.nickname);
        this.nickname = nickname;
    }

    public void updateRetrospectDay(DayOfWeek retrospectDay) { //member내에서는 닉네임의 update만 발생.
        System.out.println("-------userDto 내 update확인: "+retrospectDay+" | this값 확인: "+this.retrospectDay);
        this.retrospectDay = retrospectDay;
    }*/

    public static UserDto toDto(OAuth2User oAuth2User) {
        var attributes = oAuth2User.getAttributes();
        return UserDto.builder()
                .email((String)attributes.get("email"))
                .name((String)attributes.get("name"))
                .provider((String)attributes.get("provider"))
                .build();
    }
}
