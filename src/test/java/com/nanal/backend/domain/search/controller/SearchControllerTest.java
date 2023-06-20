package com.nanal.backend.domain.search.controller;

import com.nanal.backend.config.CommonControllerTest;
import com.nanal.backend.domain.diary.dto.req.KeywordDto;
import com.nanal.backend.domain.diary.dto.req.KeywordEmotionDto;
import com.nanal.backend.domain.search.dto.DiaryInfo;
import com.nanal.backend.domain.search.dto.req.ReqSearchDto;
import com.nanal.backend.domain.search.dto.resp.RespSearchDto;
import com.nanal.backend.domain.search.dto.RetrospectInfo;
import com.nanal.backend.domain.search.service.SearchService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.nanal.backend.domain.search.dto.DiaryInfo.*;
import static com.nanal.backend.domain.search.dto.RetrospectInfo.*;
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

@WebMvcTest(SearchController.class)
class SearchControllerTest extends CommonControllerTest {

    @MockBean
    private SearchService searchService;

    @Test
    public void 검색() throws Exception {
        //given
        ReqSearchDto input = ReqSearchDto.builder()
                .searchWord("검색단어")
                .startDate(LocalDateTime.parse("2022-11-01T00:00:00"))
                .endDate(LocalDateTime.parse("2022-11-30T00:00:00"))
                .offset(0)
                .limit(10)
                .build();

        List<String> keywordDtoList = new ArrayList<>(Arrays.asList("키워드1", "키워드2", "키워드3"));

        DiaryDto diaryDto = DiaryDto.builder()
                .diaryId(5L)
                .writeDate(LocalDateTime.parse("2022-11-15T00:00:00"))
                .content("일기 내용")
                .keywords(keywordDtoList)
                .build();

        RetrospectDto retrospectDto = RetrospectDto.builder()
                .retrospectId(10L)
                .writeDate(LocalDateTime.parse("2022-11-10T00:00:00"))
                .question("이번주 나의 모습은 어땠나요?")
                .answer("좋았어요")
                .build();

        DiaryInfo diaryInfo = new DiaryInfo();
        diaryInfo.setDiaryDtoList(new ArrayList<>(Arrays.asList(diaryDto)));
        diaryInfo.setNextDiaryCount(0);

        RetrospectInfo retrospectInfo = new RetrospectInfo();
        retrospectInfo.setRetrospectDtoList(new ArrayList<>(Arrays.asList(retrospectDto)));
        retrospectInfo.setNextRetrospectCount(0);

        RespSearchDto output = RespSearchDto.builder()
                .diaryInfo(diaryInfo)
                .retrospectInfo(retrospectInfo)
                .build();

        given(searchService.search(any())).willReturn(output);

        //when
        ResultActions actions = mockMvc.perform(
                get("/search")
                        .header("Token", "ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("searchWord", input.getSearchWord())
                        .param("startDate", input.getStartDate().toString())
                        .param("endDate", input.getEndDate().toString())
                        .param("offset", input.getOffset().toString())
                        .param("limit", input.getLimit().toString())
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
                                        parameterWithName("searchWord").description("검색할 단어"),
                                        parameterWithName("startDate").description("처음 날짜"),
                                        parameterWithName("endDate").description("마지막 날짜"),
                                        parameterWithName("offset").description("몇 번째부터 가져올지(zero based)"),
                                        parameterWithName("limit").description("몇 개씩 가져올지")
                                ),
                                responseFields(
                                        fieldWithPath("isSuccess").description("성공 여부"),
                                        fieldWithPath("code").description("상태 코드"),
                                        fieldWithPath("message").description("결과 메시지"),
                                        fieldWithPath("result.diaryInfo.existMore").description("남은 일기 존재 여부"),
                                        fieldWithPath("result.diaryInfo.nextDiaryCount").description("다음번 요청 가능 일기수"),
                                        fieldWithPath("result.diaryInfo.diaryDtoList[].diaryId").description("일기 ID"),
                                        fieldWithPath("result.diaryInfo.diaryDtoList[].writeDate").description("작성 날짜"),
                                        fieldWithPath("result.diaryInfo.diaryDtoList[].content").description("일기 내용"),
                                        fieldWithPath("result.diaryInfo.diaryDtoList[].keywords[]").description("키워드"),

                                        fieldWithPath("result.retrospectInfo.existMore").description("남은 회고 존재 여부"),
                                        fieldWithPath("result.retrospectInfo.nextRetrospectCount").description("다음번 요청 가능 회고수"),
                                        fieldWithPath("result.retrospectInfo.retrospectDtoList[].retrospectId").description("회고 ID"),
                                        fieldWithPath("result.retrospectInfo.retrospectDtoList[].writeDate").description("작성 날짜"),
                                        fieldWithPath("result.retrospectInfo.retrospectDtoList[].question").description("회고 목적별 질문"),
                                        fieldWithPath("result.retrospectInfo.retrospectDtoList[].answer").description("질문에 대한 답변")
                                )
                        )
                );


    }

}