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

        private String name;

        private String email;

        private String nickname;

        private String gender;

        private String age;
    }
}
