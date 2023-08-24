package com.nanal.backend.domain.sponsor.controller;

import com.nanal.backend.config.CommonControllerTest;
import com.nanal.backend.domain.sponsor.dto.req.ReqCheckSponsorDto;
import com.nanal.backend.domain.sponsor.service.SponsorService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SponsorController.class)
public class SponsorControllerTest extends CommonControllerTest {

    @MockBean
    SponsorService sponsorService;

    @Test
    public void 후원자_인증() throws Exception {
        //given
        String code = "code";
        ReqCheckSponsorDto reqCheckSponsorDto = new ReqCheckSponsorDto(code);
        willDoNothing().given(sponsorService).checkSponsor(any(), any()); //void일때는 willDoNothing 사용

        //when
        ResultActions actions = mockMvc.perform(
                post("/sponsor")
                        .header("Token", "ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqCheckSponsorDto))
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
                                        fieldWithPath("code").description("인증 코드")
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
