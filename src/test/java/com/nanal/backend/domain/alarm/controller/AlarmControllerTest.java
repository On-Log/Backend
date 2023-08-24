package com.nanal.backend.domain.alarm.controller;

import com.nanal.backend.config.CommonControllerTest;
import com.nanal.backend.domain.alarm.dto.ReqUpdateDiaryAlarm;
import com.nanal.backend.domain.alarm.dto.ReqUpdateRetrospectAlarm;
import com.nanal.backend.domain.alarm.service.AlarmService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AlarmController.class)
class AlarmControllerTest extends CommonControllerTest {

    @MockBean
    private AlarmService alarmService;

    @Test
    public void 일기_알림_변경() throws Exception {
        //given
        ReqUpdateDiaryAlarm input = new ReqUpdateDiaryAlarm(true, "20:00");

        String body = objectMapper.writeValueAsString(input);

        willDoNothing().given(alarmService).updateDiaryAlarm(any(), any());

        //when
        ResultActions actions = mockMvc.perform(
                put("/alarm/diary")
                        .header("Token", "ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
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
                                        fieldWithPath("diaryAlarmActive").description("일기 알림 활성화 여부"),
                                        fieldWithPath("diaryAlarmTime").description("일기 알림 시간")
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
    public void 회고_알림_변경() throws Exception {
        //given
        ReqUpdateRetrospectAlarm input = new ReqUpdateRetrospectAlarm(true, "20:00");

        String body = objectMapper.writeValueAsString(input);

        willDoNothing().given(alarmService).updateRetrospectAlarm(any(), any());

        //when
        ResultActions actions = mockMvc.perform(
                put("/alarm/retrospect")
                        .header("Token", "ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
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
                                        fieldWithPath("retrospectAlarmActive").description("회고 알림 활성화 여부"),
                                        fieldWithPath("retrospectAlarmTime").description("회고 알림 시간")
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