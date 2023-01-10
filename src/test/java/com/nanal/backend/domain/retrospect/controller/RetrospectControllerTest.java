package com.nanal.backend.domain.retrospect.controller;

import com.nanal.backend.config.CommonControllerTest;
import com.nanal.backend.domain.retrospect.dto.resp.RespGetInfoDto;
import com.nanal.backend.domain.retrospect.service.RetrospectService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RetrospectController.class)
public class RetrospectControllerTest extends CommonControllerTest {

    @MockBean
    private RetrospectService retrospectService;

    @Test
    public void 회고_탭() throws Exception {
        //given
        String currentDate = "2023-01-22T00:00:00";
        String selectDate = "2023-01-13T00:00:00";
        List<String> weekkeyword1 = new ArrayList<>(Arrays.asList("1주차 키워드1, 1주차 키워드2")); //분류한 키워드를 주차별로 나누는 리스트
        List<String> weekkeyword2 = new ArrayList<>(Arrays.asList("1주차 키워드3")); //주차 별 키워드
        List<String> weekkeyword3 = new ArrayList<>(Arrays.asList("1주차 키워드4")); //주차 별 키워드
        List<List<String>> classifykeyword1 = new ArrayList<>(Arrays.asList(weekkeyword1)); //키워드 분류하는 리스트
        List<List<String>> classifykeyword2 = new ArrayList<>(Arrays.asList(weekkeyword2));
        List<List<String>> classifykeyword3 = new ArrayList<>(Arrays.asList(weekkeyword3));

        List<List<List<String>>> existRetrospectKeyword = new ArrayList<>(Arrays.asList(classifykeyword1, classifykeyword2, classifykeyword3));
        RespGetInfoDto respGetInfoDto = new RespGetInfoDto(new ArrayList<>(Arrays.asList("자아탐색")), 6, existRetrospectKeyword);
        given(retrospectService.getInfo(any(), any())).willReturn(respGetInfoDto);

        //when
        ResultActions actions = mockMvc.perform(
                get("/retrospect")
                        .header("Token", "ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("currentDate", currentDate)
                        .param("selectDate", selectDate)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                requestHeaders(
                                        headerWithName("Token").description("접근 토큰")
                                ),
                                requestParameters(
                                        parameterWithName("currentDate").description("현재 날짜"),
                                        parameterWithName("selectDate").description("선택 날짜")
                                ),
                                responseFields(
                                        fieldWithPath("isSuccess").description("성공 여부"),
                                        fieldWithPath("code").description("상태 코드"),
                                        fieldWithPath("message").description("결과 메시지"),
                                        fieldWithPath("result.existRetrospect").description("회고 목적"),
                                        fieldWithPath("result.betweenDate").description("다음 회고까지 남은 날"),
                                        fieldWithPath("result.existRetrospectKeyword[].[].[]").description("키워드 분류")

                                )
                        )
                );

    }


}
