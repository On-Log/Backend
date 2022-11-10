package com.nanal.backend.domain.token;

import com.nanal.backend.config.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class TokenController {

    private final TokenService tokenService;

    @GetMapping("/token/reissue")
    public CommonResponse<Token> refreshAuth(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("RefreshToken");

        // refresh 토큰이 유효한지 확인
        if (token != null && tokenService.verifyToken(token)) {
            String email = tokenService.getUid(token);
            // 토큰 새로 받아오기
            Token reissueToken = tokenService.tokenReissue(token);

            return new CommonResponse<>(reissueToken);
        }

        // refresh 토큰이 유효하지 않을 경우
        throw new RuntimeException();
    }
}
