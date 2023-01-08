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
        private String profileImage;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KakaoAccount {
        private String email;
        private String name;
        private String gender;
    }
}
