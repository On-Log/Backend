package com.nanal.backend.domain;

import com.nanal.backend.config.CommonControllerTest;
import com.nanal.backend.domain.onboarding.controller.OnBoardingController;
import com.nanal.backend.domain.onboarding.dto.ReqSetRetrospectDayDto;
import com.nanal.backend.domain.onboarding.service.OnBoardingService;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OnBoardingController.class)
public class OnBoardingControllerTest extends CommonControllerTest {

    @MockBean
    private OnBoardingService onBoardingService;

    @Test
    public void 온보딩_회고일_변경() throws Exception {
        //given
        ReqSetRetrospectDayDto input = new ReqSetRetrospectDayDto("SUNDAY");

        String body = objectMapper.writeValueAsString(input);

        willDoNothing().given(onBoardingService).setRetrospectDay(any(), any());

        //when
        ResultActions actions = mockMvc.perform(
                post("/onBoarding")
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
                                        fieldWithPath("retrospectDay").description("설정할 회고요일")
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
