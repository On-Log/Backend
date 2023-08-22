package com.nanal.backend.domain.retrospect.controller;

import com.nanal.backend.config.CommonControllerTest;
import com.nanal.backend.domain.retrospect.dto.req.ClassifyKeywordDto;
import com.nanal.backend.domain.retrospect.v2.dto.req.ReqEditRetroDto;
import com.nanal.backend.domain.retrospect.dto.resp.*;
import com.nanal.backend.domain.retrospect.v2.dto.resp.RespGetInfoDto;
import com.nanal.backend.domain.retrospect.v2.dto.resp.RespGetRetroDto;
import com.nanal.backend.domain.retrospect.v2.controller.RetrospectControllerV2;
import com.nanal.backend.domain.retrospect.v2.service.RetrospectServiceV2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

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
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RetrospectControllerV2.class)
public class RetrospectControllerTestV2 extends CommonControllerTest {
    @MockBean
    private RetrospectServiceV2 retrospectServiceV2;

    @Test
    public void 회고_탭() throws Exception {
        //given
        String fromDate = "2023-04-01T00:00:00";
        String toDate = "2023-04-30T00:00:00";
        List<ClassifyKeywordDto> firstweek1= new ArrayList<>(Arrays.asList(
                new ClassifyKeywordDto("앱출시")
        ));
        List<ClassifyKeywordDto> firstweek2 = new ArrayList<>(Arrays.asList(
                new ClassifyKeywordDto("창업")
        ));

        List<ClassifyKeywordDto> secondweek1 = new ArrayList<>(Arrays.asList(
                new ClassifyKeywordDto("면접"),
                new ClassifyKeywordDto("자격증시험")
        ));

        List<ClassifyKeywordDto> secondweek2  = new ArrayList<>(Arrays.asList(
                new ClassifyKeywordDto("알바")
        ));


        List<ClassifyKeywordDto>  thirdweek1 = new ArrayList<>(Arrays.asList(
                new ClassifyKeywordDto("봉사")
        ));
        List<ClassifyKeywordDto>  thirdweek2 = new ArrayList<>(Arrays.asList(
                new ClassifyKeywordDto("취준")
        ));

        List<ClassifyDto> classifyDtos1 = new ArrayList<>(Arrays.asList(
                new ClassifyDto(firstweek1),
                new ClassifyDto(firstweek2)
        ));

        List<ClassifyDto> classifyDtos2 = new ArrayList<>(Arrays.asList(
                new ClassifyDto(secondweek1),
                new ClassifyDto(secondweek2)
        ));

        List<ClassifyDto> classifyDtos3 = new ArrayList<>(Arrays.asList(
                new ClassifyDto(thirdweek1),
                new ClassifyDto(thirdweek2)
        ));

        List<RespGetClassifiedKeywordDto> respGetClassifiedKeywordDtos = new ArrayList<>(Arrays.asList(
                new RespGetClassifiedKeywordDto(classifyDtos1, "그때 그대로 의미있었던 행복한 기억"),
                new RespGetClassifiedKeywordDto(classifyDtos2, "나를 힘들게 했지만 도움이 된 기억"),
                new RespGetClassifiedKeywordDto(classifyDtos3, "돌아보니, 다른 의미로 다가온 기억")
        ));

        RespGetInfoDto respGetInfoDto = new RespGetInfoDto("사용자 닉네임", new ArrayList<>(Arrays.asList("자아탐색", "성취확인")), new ArrayList<>(Arrays.asList(1L,2L)), 6, true, respGetClassifiedKeywordDtos);
        given(retrospectServiceV2.getInfo(any(), any())).willReturn(respGetInfoDto);

        //when
        ResultActions actions = mockMvc.perform(
                get("/retrospect/v2")
                        .header("Token", "ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("fromDate", fromDate)
                        .param("toDate", toDate)
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
                                        parameterWithName("fromDate").description("처음 날짜"),
                                        parameterWithName("toDate").description("마지막 날짜")
                                ),
                                responseFields(
                                        fieldWithPath("isSuccess").description("성공 여부"),
                                        fieldWithPath("code").description("상태 코드"),
                                        fieldWithPath("message").description("결과 메시지"),
                                        fieldWithPath("result.nickname").description("사용자 닉네임"),
                                        fieldWithPath("result.retrospectGoal").description("회고 목적"),
                                        fieldWithPath("result.retrospectId").description("회고 ID"),
                                        fieldWithPath("result.betweenDate").description("다음 회고까지 남은 날"),
                                        fieldWithPath("result.countRetrospect").description("회고 개수 체크. 5개 이상일 시, false 보내 줌"),
                                        fieldWithPath("result.keywordList[].val").description("키워드 분류 주제"),
                                        fieldWithPath("result.keywordList[].classify[].weeklykeywords[].keyword").description("주차별 키워드. 1차 회고부터 순서대로임")

                                )
                        )
                );

    }

    @Test
    public void 회고_조회() throws Exception {
        //given
        List<RetrospectContentDto> retrospectContentDtos = new ArrayList<>(Arrays.asList(new RetrospectContentDto("이번주 나의 모습은 어땠나요?", "답변1"),
                new RetrospectContentDto("다른 내 모습도 들려줄래요? 이번주에 찾은 의외의 내 모습이 있다면요?", "답변2"), new RetrospectContentDto("다음주에도 유지하고 싶은 나의 모습이 있을까요? 혹은 새롭게 찾고 싶은 나의 모습이 있다면 무엇인가요?", "답변3")));
        List<RetrospectKeywordDto> retrospectKeywordDtos = new ArrayList<>(Arrays.asList(new RetrospectKeywordDto("그때 그대로 의미있었던 행복한 기억", "키워드1"),
                new RetrospectKeywordDto("나를 힘들게 했지만 도움이 된 기억", "키워드2"),
                new RetrospectKeywordDto("돌아보니, 다른 의미로 다가온 기억", "키워드3")));
        RespGetRetroDto respGetRetroDto = new RespGetRetroDto(LocalDateTime.parse("2023-01-18T00:00:00"), retrospectContentDtos, retrospectKeywordDtos,1);
        given(retrospectServiceV2.getRetro(any())).willReturn(respGetRetroDto);

        //when
        ResultActions actions = mockMvc.perform(
                get("/retrospect/view/v2")
                        .header("Token", "ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("retrospectId", String.valueOf(1L))
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
                                        parameterWithName("retrospectId").description("회고 ID")
                                ),
                                responseFields(
                                        fieldWithPath("isSuccess").description("성공 여부"),
                                        fieldWithPath("code").description("상태 코드"),
                                        fieldWithPath("message").description("결과 메시지"),
                                        fieldWithPath("result.writeDate").description("회고 작성 날짜"),
                                        fieldWithPath("result.contents[].question").description("회고 목적별 질문"),
                                        fieldWithPath("result.contents[].answer").description("질문에 대한 답변"),
                                        fieldWithPath("result.keywords[].classify").description("회고 과정에서 키워드 분류 기준(감정 분리수거 기능)"),
                                        fieldWithPath("result.keywords[].keyword").description("분류된 키워드"),
                                        fieldWithPath("result.week").description("주차")
                                )
                        )
                );
    }

    @Test
    public void 회고_수정() throws Exception {
        //given
        ReqEditRetroDto reqEditRetroDto = new ReqEditRetroDto("수정답변", 1L, 0);
        willDoNothing().given(retrospectServiceV2).editRetrospect(any(), any());

        //when
        ResultActions actions = mockMvc.perform(
                put("/retrospect/v2")
                        .header("Token", "ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqEditRetroDto))
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
                                        fieldWithPath("answer").description("수정된 회고 질문에 대한 답변"),
                                        fieldWithPath("retrospectId").description("회고 ID"),
                                        fieldWithPath("index").description("수정할 회고 질문 (index로 되어있어서 첫번째 질문은 0, 두번째 질문은 1 이런 식으로 보내야 함)")
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
    public void 회고_삭제() throws Exception {
        //given
        willDoNothing().given(retrospectServiceV2).deleteRetro(any(), any());

        //when
        ResultActions actions = mockMvc.perform(
                delete("/retrospect/v2")
                        .header("Token", "ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("retrospectId", String.valueOf(1L))
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
                                        parameterWithName("retrospectId").description("회고 ID")
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
