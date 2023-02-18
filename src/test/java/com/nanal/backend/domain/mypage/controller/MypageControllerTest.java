package com.nanal.backend.domain.mypage.controller;

import com.nanal.backend.config.CommonControllerTest;
import com.nanal.backend.domain.mypage.dto.req.ReqEditNicknameDto;
import com.nanal.backend.domain.mypage.dto.req.ReqEditRetrospectDayDto;
import com.nanal.backend.domain.mypage.dto.req.ReqWithdrawMembership;
import com.nanal.backend.domain.mypage.dto.req.ReqWithdrawMembership.Reason;
import com.nanal.backend.domain.mypage.dto.resp.*;
import com.nanal.backend.domain.mypage.service.MypageService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
                                        fieldWithPath("result.nickname").description("유저 닉네임"),
                                        fieldWithPath("result.email").description("유저 이메일"),
                                        fieldWithPath("result.retrospectDay").description("유저의 회고일")
                                )
                        )
                );
    }

    @Test
    public void 닉네임_변경() throws Exception {
        //given
        ReqEditNicknameDto input = new ReqEditNicknameDto("변경 닉네임");
        willDoNothing().given(mypageService).updateNickname(any(), any());

        //when
        ResultActions actions = mockMvc.perform(
                put("/mypage/nickname")
                        .header("Token", "ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
        );

        //then
        actions
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestHeaders(
                                        headerWithName("Token").description("접근 토큰")
                                ),
                                requestFields(
                                        fieldWithPath("nickname").description("변경한 닉네임")
                                ),
                                responseFields(
                                        fieldWithPath("isSuccess").description("성공 여부"),
                                        fieldWithPath("code").description("상태 코드"),
                                        fieldWithPath("message").description("결과 메시지")
                                )
                        )
                );
    }

    @Test
    public void 회고일_변경_가능_여부() throws Exception {
        //given
        RespCheckChangeAvailability output = RespCheckChangeAvailability.changeable(
                LocalDateTime.of(2023, 1, 12, 6, 0),
                DayOfWeek.SUNDAY);

        given(mypageService.checkChangeAvailability(any())).willReturn(output);

        //when
        ResultActions actions = mockMvc.perform(
                get("/mypage/retrospect")
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
                                        fieldWithPath("result.nextChangeableDate").description("다음 회고 변경 가능일"),
                                        fieldWithPath("result.curRetrospectDay").description("현재 회고 요일")
                                )
                        )
                );

    }

    @Test
    public void 회고일_변경_불가능() throws Exception {
        //given
        RespCheckChangeAvailability output = RespCheckChangeAvailability.unchangeable(
                LocalDateTime.of(2023, 1, 15, 8, 0));

        given(mypageService.checkChangeAvailability(any())).willReturn(output);

        //when
        ResultActions actions = mockMvc.perform(
                get("/mypage/retrospect")
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
                                        fieldWithPath("result.changeableDate").description("회고 변경 가능일")
                                )
                        )
                );

    }

    @Test
    public void 회고일_변경() throws Exception {
        //given
        ReqEditRetrospectDayDto reqEditRetrospectDayDto = new ReqEditRetrospectDayDto(LocalDate.of(2023, 1, 12).getDayOfWeek().toString());
        willDoNothing().given(mypageService).updateRetrospectDay(any(), any());

        //when
        ResultActions actions = mockMvc.perform(
                put("/mypage/retrospect")
                        .header("Token", "ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqEditRetrospectDayDto))
        );

        //then
        actions
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestHeaders(
                                        headerWithName("Token").description("접근 토큰")
                                ),
                                requestFields(
                                        fieldWithPath("retrospectDay").description("변경한 회고일")
                                ),
                                responseFields(
                                        fieldWithPath("isSuccess").description("성공 여부"),
                                        fieldWithPath("code").description("상태 코드"),
                                        fieldWithPath("message").description("결과 메시지")
                                )
                        )
                );
    }

    @Test
    public void 서비스_사용기간_조회() throws Exception {
        //given
        RespGetServiceLife output = RespGetServiceLife.builder()
                .serviceLife(92)
                .build();
        given(mypageService.getServiceLife(any())).willReturn(output);

        //when
        ResultActions actions = mockMvc.perform(
                get("/mypage/service-life")
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
                                        fieldWithPath("result.serviceLife").description("서비스 사용기간")
                                )
                        )
                );

    }

    @Test
    public void 로그아웃() throws Exception {
        //given
        willDoNothing().given(mypageService).logout(any());

        //when
        ResultActions actions = mockMvc.perform(
                get("/mypage/logout")
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
                                        fieldWithPath("message").description("결과 메시지")
                                )
                        )
                );

    }

    @Test
    public void 회원탈퇴() throws Exception {
        //given
        List<Reason> reasons = new ArrayList<>(Arrays.asList(
                new Reason("나날의 효과를 실감하지 못했어요"),
                new Reason("앱 사용이 너무 불편해요"),
                new Reason("나나리랑 싸웠습니다.")
        ));
        String detail = "회원탈퇴의 상세한 이유입니다.";

        ReqWithdrawMembership input = ReqWithdrawMembership.builder()
                .reasons(reasons)
                .detail(detail)
                .build();

        willDoNothing().given(mypageService).withdrawMembership(any(), any());

        //when
        ResultActions actions = mockMvc.perform(
                delete("/mypage/withdrawal")
                        .header("Token", "ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
        );

        //then
        actions
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestHeaders(
                                        headerWithName("Token").description("접근 토큰")
                                ),
                                requestFields(
                                        fieldWithPath("reasons[].content").description("회원탈퇴 이유"),
                                        fieldWithPath("detail").description("회원탈퇴 상세 내용")
                                ),
                                responseFields(
                                        fieldWithPath("isSuccess").description("성공 여부"),
                                        fieldWithPath("code").description("상태 코드"),
                                        fieldWithPath("message").description("결과 메시지")
                                )
                        )
                );
    }
}