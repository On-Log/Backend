package com.nanal.backend.domain.auth;

import com.nanal.backend.domain.auth.dto.ReqSignUpDto;
import com.nanal.backend.global.auth.token.Token;
import com.nanal.backend.global.response.CommonResponse;
import com.nanal.backend.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public CommonResponse<?> signUp(ReqSignUpDto reqSignUpDto) {

        authService.signUp(reqSignUpDto);

        return new CommonResponse<>(ErrorCode.SUCCESS);
    }


}
