package com.nanal.backend.domain.mypage.controller;

import com.nanal.backend.config.CommonControllerTest;
import com.nanal.backend.domain.mypage.dto.resp.RespGetUserDto;
import com.nanal.backend.domain.mypage.service.MypageService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MypageController.class)
public class MypageControllerTest extends CommonControllerTest {

    @MockBean
    private MypageService mypageService;

    @Test
    public void 마이페이지_탭() throws Exception {
        //given
        RespGetUserDto respGetUserDto = new RespGetUserDto("사용자 닉네임", "유저 이메일", LocalDate.of(2023, 1, 12).getDayOfWeek());
        given(mypageService.getUser(any())).willReturn(respGetUserDto);

        //when
        ResultActions actions = mockMvc.perform(
                get("/mypage")
                        .header("Token", "ACCESS_TOKEN")
        );

        //then
        actions
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestHeaders(
                                        headerWithName("Token").description("접근 토큰")
                                ),
                                responseFields(
                                        fieldWithPath("isSuccess").description("성공 여부"),
                                        fieldWithPath("code").description("상태 코드"),
                                        fieldWithPath("message").description("결과 메시지"),
                                        fieldWithPath("result.userNickname").description("유저 닉네임"),
                                        fieldWithPath("result.userEmail").description("유저 이메일"),
                                        fieldWithPath("result.userRetrospectDay").description("유저의 회고일")
                                )
                        )
                );
    }
}