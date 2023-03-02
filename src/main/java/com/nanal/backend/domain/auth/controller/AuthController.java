package com.nanal.backend.domain.auth.controller;

import com.nanal.backend.domain.auth.dto.LoginInfo;
import com.nanal.backend.domain.auth.dto.req.ReqAuthDto;
import com.nanal.backend.domain.auth.dto.req.ReqEmailConfirmDto;
import com.nanal.backend.domain.auth.dto.req.ReqRegisterDto;
import com.nanal.backend.domain.auth.dto.resp.RespEmailConfirmDto;
import com.nanal.backend.domain.auth.service.AuthService;
import com.nanal.backend.domain.auth.service.EmailService;
import com.nanal.backend.global.response.CommonResponse;
import com.nanal.backend.global.response.ErrorCode;
import com.nanal.backend.global.security.jwt.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;

    /**
     * 일반 회원가입 기능
     */
    @PostMapping(value = "/auth/register")
    public CommonResponse<?> generalRegister(@RequestBody ReqRegisterDto reqRegisterDto) {

        authService.generalRegister(reqRegisterDto);

        return new CommonResponse<>(ErrorCode.SUCCESS);
    }

    /**
     * 이메일 인증
     */
    @PostMapping("/auth/emailConfirm")
    public CommonResponse<?> emailConfirm(@RequestBody ReqEmailConfirmDto reqEmailConfirmDto) throws Exception {

        RespEmailConfirmDto respEmailConfirmDto = emailService.sendSimpleMessage(reqEmailConfirmDto.getEmail());

        return new CommonResponse<>(respEmailConfirmDto);
    }

    /**
     * 일반 로그인 기능
     */
    @PostMapping(value = "/auth/login")
    public CommonResponse<?> generalLogin(@RequestBody ReqRegisterDto reqRegisterDto) {

        LoginInfo token = authService.generalLogin(reqRegisterDto);

        return new CommonResponse<>(token);
    }

    /**
     * 비밀번호 찾기
     */


    /**
     * NAVER 소셜 로그인 기능
     */
    @PostMapping(value = "/auth/naver")
    public CommonResponse<?> naverAuth(@RequestBody ReqAuthDto reqAuthDto, HttpServletRequest request) {

        // 최초 로그인 - 회원가입 후 토큰 발행.
        // 기존 유저 - 토큰 발행.
        LoginInfo loginInfo = authService.commonAuth(reqAuthDto.getAccessToken(), request.getRequestURI());

        if(loginInfo.getOnBoarding())
            return new CommonResponse<>(ErrorCode.SUCCESS_BUT, loginInfo);
        else
            return new CommonResponse<>(loginInfo);
    }

    /**
     * KAKAO 소셜 로그인 기능
     */
    @PostMapping(value = "/auth/kakao")
    public CommonResponse<?> kakaoAuth(@RequestBody ReqAuthDto reqAuthDto, HttpServletRequest request) {

        // 최초 로그인 - 회원가입 후 토큰 발행.
        // 기존 유저 - 토큰 발행.
        LoginInfo loginInfo = authService.commonAuth(reqAuthDto.getAccessToken(), request.getRequestURI());

        if(loginInfo.getOnBoarding())
            return new CommonResponse<>(ErrorCode.SUCCESS_BUT, loginInfo);
        else
            return new CommonResponse<>(loginInfo);
    }

    /**
     * GOOGLE 소셜 로그인 기능
     */
    @PostMapping(value = "/auth/google")
    public CommonResponse<?> googleAuth(@RequestBody ReqAuthDto reqAuthDto, HttpServletRequest request) {

        // 최초 로그인 - 회원가입 후 토큰 발행.
        // 기존 유저 - 토큰 발행.
        LoginInfo loginInfo = authService.commonAuth(reqAuthDto.getAccessToken(), request.getRequestURI());

        if(loginInfo.getOnBoarding())
            return new CommonResponse<>(ErrorCode.SUCCESS_BUT, loginInfo);
        else
            return new CommonResponse<>(loginInfo);
    }


    /**
     * Token 재발급
     */
    @GetMapping("/auth/reissue")
    public CommonResponse<Token> reissue(HttpServletRequest request) {

        // 헤더로부터 RefreshToken 추출.
        String token = request.getHeader("RefreshToken");
        // 토큰 재발행
        Token newToken = authService.reissue(token);

        return new CommonResponse<>(newToken);
    }
}
