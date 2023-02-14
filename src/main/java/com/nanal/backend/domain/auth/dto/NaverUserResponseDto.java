package com.nanal.backend.domain.auth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class NaverUserResponseDto {

    private Response response;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        private String id;
        private String email;
        private String nickname;
        private String gender;
        private String age;

    }
    public void adaptResponse() {
        if(response.gender == null || response.gender.isBlank()) response.gender = "undef";
        if(response.age == null || response.age.isBlank()) response.age = "undef";
        if(response.email.length() > 50) response.email = response.email.substring(0, 50);
        if(response.nickname.length() > 7) response.nickname = response.nickname.substring(0, 7);
    }
}
