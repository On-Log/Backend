package com.nanal.backend.domain.auth.controller;

import com.nanal.backend.config.CommonControllerTest;
import com.nanal.backend.domain.auth.dto.LoginInfo;
import com.nanal.backend.domain.auth.dto.req.ReqAuthDto;
import com.nanal.backend.domain.auth.service.AuthService;
import com.nanal.backend.domain.auth.service.EmailService;
import com.nanal.backend.global.security.jwt.Token;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest extends CommonControllerTest {

    @MockBean
    private AuthService authService;

    @MockBean
    private EmailService emailService;

    @Test
    public void Naver_소셜_로그인() throws Exception {
        //given
        ReqAuthDto input = ReqAuthDto.builder()
                .accessToken("Platform_ACCESS_TOKEN")
                .build();

        String body = objectMapper.writeValueAsString(input);

        LoginInfo output = LoginInfo.builder()
                .nickname("유저 닉네임")
                .token("SERVER_ACCESS_TOKEN")
                .refreshToken("SERVER_REFRESH_TOKEN")
                .onBoarding(false)
                .build();

        given(authService.commonAuth(any(), any())).willReturn(output);

        //when
        ResultActions actions = mockMvc.perform(
                post("/auth/naver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestFields(
                                        fieldWithPath("accessToken").description("사용자 정보 접근용 플랫폼 Token")
                                ),
                                responseFields(
                                        fieldWithPath("isSuccess").description("성공 여부"),
                                        fieldWithPath("code").description("상태 코드"),
                                        fieldWithPath("message").description("결과 메시지"),
                                        fieldWithPath("result.nickname").description("유저 닉네임"),
                                        fieldWithPath("result.token").description("서버 접근용 Token"),
                                        fieldWithPath("result.refreshToken").description("서버 접근용 Refresh Token")
                                )
                        )
                );
    }

    @Test
    public void Kakao_소셜_로그인() throws Exception {
        //given
        ReqAuthDto input = ReqAuthDto.builder()
                .accessToken("PLATFORM_ACCESS_TOKEN")
                .build();

        String body = objectMapper.writeValueAsString(input);

        LoginInfo output = LoginInfo.builder()
                .nickname("유저 닉네임")
                .token("SERVER_ACCESS_TOKEN")
                .refreshToken("SERVER_REFRESH_TOKEN")
                .onBoarding(false)
                .build();

        given(authService.commonAuth(any(), any())).willReturn(output);

        //when
        ResultActions actions = mockMvc.perform(
                post("/auth/kakao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestFields(
                                        fieldWithPath("accessToken").description("사용자 정보 접근용 플랫폼 Token")
                                ),
                                responseFields(
                                        fieldWithPath("isSuccess").description("성공 여부"),
                                        fieldWithPath("code").description("상태 코드"),
                                        fieldWithPath("message").description("결과 메시지"),
                                        fieldWithPath("result.nickname").description("유저 닉네임"),
                                        fieldWithPath("result.token").description("서버 접근용 Token"),
                                        fieldWithPath("result.refreshToken").description("서버 접근용 Refresh Token")
                                )
                        )
                );
    }

    @Test
    public void Google_소셜_로그인() throws Exception {
        //given
        ReqAuthDto input = ReqAuthDto.builder()
                .accessToken("Platform_ACCESS_TOKEN")
                .build();

        String body = objectMapper.writeValueAsString(input);

        LoginInfo output = LoginInfo.builder()
                .nickname("유저 닉네임")
                .token("SERVER_ACCESS_TOKEN")
                .refreshToken("SERVER_REFRESH_TOKEN")
                .onBoarding(false)
                .build();

        given(authService.commonAuth(any(), any())).willReturn(output);

        //when
        ResultActions actions = mockMvc.perform(
                post("/auth/google")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestFields(
                                        fieldWithPath("accessToken").description("사용자 정보 접근용 플랫폼 Token")
                                ),
                                responseFields(
                                        fieldWithPath("isSuccess").description("성공 여부"),
                                        fieldWithPath("code").description("상태 코드"),
                                        fieldWithPath("message").description("결과 메시지"),
                                        fieldWithPath("result.nickname").description("유저 닉네임"),
                                        fieldWithPath("result.token").description("서버 접근용 Token"),
                                        fieldWithPath("result.refreshToken").description("서버 접근용 Refresh Token")
                                )
                        )
                );
    }

    @Test
    public void Token_재발급() throws Exception {
        //given
        Token output = Token.builder()
                .token("SERVER_ACCESS_TOKEN")
                .refreshToken("SERVER_REFRESH_TOKEN")
                .build();

        given(authService.reissue(any())).willReturn(output);

        //when
        ResultActions actions = mockMvc.perform(
                get("/auth/reissue")
                        .header("RefreshToken", "REFRESH_TOKEN")
        );

        //then
        actions
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestHeaders(
                                        headerWithName("RefreshToken").description("토큰 재발급용 RefreshToken")
                                ),
                                responseFields(
                                        fieldWithPath("isSuccess").description("성공 여부"),
                                        fieldWithPath("code").description("상태 코드"),
                                        fieldWithPath("message").description("결과 메시지"),
                                        fieldWithPath("result.token").description("서버 접근용 Token"),
                                        fieldWithPath("result.refreshToken").description("서버 접근용 Refresh Token")
                                )
                        )
                );
    }
}