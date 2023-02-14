package com.nanal.backend.domain.auth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GoogleUserResponseDto {

    private String aud;
    private String sub;
    private String email;
    private String name;
    private String picture;

    public void adaptResponse() {
        if(email.length() > 50) email = email.substring(0, 50);
        if(name.length() > 7) name = name.substring(0, 7);
    }
}