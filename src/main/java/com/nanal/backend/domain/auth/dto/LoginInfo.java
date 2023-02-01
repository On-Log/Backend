package com.nanal.backend.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nanal.backend.global.security.jwt.Token;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginInfo {

    private String nickname;

    private String token;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String refreshToken;

    public LoginInfo(String nickname, Token token) {
        this.nickname = nickname;
        this.token = token.getToken();
        this.refreshToken = token.getRefreshToken();
    }
}
