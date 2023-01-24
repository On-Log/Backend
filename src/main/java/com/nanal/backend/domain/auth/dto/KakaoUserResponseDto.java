package com.nanal.backend.domain.auth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class KakaoUserResponseDto {
    private Long id;
    private Properties properties;
    private KakaoAccount kakaoAccount;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class Properties {

        private String nickname;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KakaoAccount {

        private String email;
        private String gender;
        private String ageRange;
    }
    public void adaptResponse() {
        if(kakaoAccount.gender == null || kakaoAccount.gender.isBlank()) kakaoAccount.gender = "undef";
        if(kakaoAccount.ageRange == null || kakaoAccount.ageRange.isBlank()) kakaoAccount.ageRange = "undef";
        if(kakaoAccount.email.length() > 50) kakaoAccount.email = kakaoAccount.email.substring(0, 50);
        if(properties.nickname.length() > 7) properties.nickname = properties.nickname.substring(0, 7);
    }
}
