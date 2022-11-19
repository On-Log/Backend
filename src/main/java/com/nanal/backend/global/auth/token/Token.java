package com.nanal.backend.global.auth.token;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Token {
    private String token;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String refreshToken;

    public Token(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
