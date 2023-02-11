package com.nanal.backend.domain.search.controller;

import com.nanal.backend.config.CommonControllerTest;
import com.nanal.backend.domain.diary.dto.req.KeywordDto;
import com.nanal.backend.domain.diary.dto.req.KeywordEmotionDto;
import com.nanal.backend.domain.retrospect.dto.resp.RetrospectContentDto;
import com.nanal.backend.domain.retrospect.dto.resp.RetrospectKeywordDto;
import com.nanal.backend.domain.search.dto.ReqSearchDto;
import com.nanal.backend.domain.search.dto.RespSearchDto;
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

import static com.nanal.backend.domain.search.dto.RespSearchDto.*;
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

        List<KeywordEmotionDto> keywordEmotionDtoList = new ArrayList<>(Arrays.asList(
                new KeywordEmotionDto("아쉬움"),
                new KeywordEmotionDto("복잡"),
                new KeywordEmotionDto("기대")
        ));
        List<KeywordDto> keywordDtoList = new ArrayList<>(Arrays.asList(
                new KeywordDto("검색단어", keywordEmotionDtoList),
                new KeywordDto("검색단어", keywordEmotionDtoList),
                new KeywordDto("검색단어", keywordEmotionDtoList)
        ));
        DiaryDto diaryDto = DiaryDto.builder()
                .writeDate(LocalDateTime.parse("2022-11-15T00:00:00"))
                .content("검색단어")
                .editStatus(true)
                .keywords(keywordDtoList)
                .build();

        List<RetrospectContentDto> retrospectContentDtoList = new ArrayList<>(Arrays.asList(
                new RetrospectContentDto("답변1", "검색단어"),
                new RetrospectContentDto("답변2", "검색단어"),
                new RetrospectContentDto("답변3", "검색단어")
        ));
        List<RetrospectKeywordDto> retrospectKeywordDtoList = new ArrayList<>(Arrays.asList(
                new RetrospectKeywordDto("그때 그대로 의미있었던 행복한 기억", "키워드1"),
                new RetrospectKeywordDto("나를 힘들게 했지만 도움이 된 기억", "키워드2"),
                new RetrospectKeywordDto("돌아보니, 다른 의미로 다가온 기억", "키워드3")
        ));
        RetrospectDto retrospectDto = RetrospectDto.builder()
                .writeDate(LocalDateTime.parse("2022-11-10T00:00:00"))
                .contents(retrospectContentDtoList)
                .keywords(retrospectKeywordDtoList)
                .build();

        RespSearchDto output = new RespSearchDto();
        output.setDiaryDtoList(new ArrayList<>(Arrays.asList(diaryDto)));
        output.setRetrospectDtoList(new ArrayList<>(Arrays.asList(retrospectDto)));

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
                                        fieldWithPath("result.diaryDtoList[].writeDate").description("작성 날짜"),
                                        fieldWithPath("result.diaryDtoList[].content").description("일기 내용"),
                                        fieldWithPath("result.diaryDtoList[].editStatus").description("수정 가능 여부"),
                                        fieldWithPath("result.diaryDtoList[].keywords[].keyword").description("키워드"),
                                        fieldWithPath("result.diaryDtoList[].keywords[].keywordEmotions[].emotion").description("감정어"),

                                        fieldWithPath("result.retrospectDtoList[].writeDate").description("작성 날짜"),
                                        fieldWithPath("result.retrospectDtoList[].contents[].question").description("회고 목적별 질문"),
                                        fieldWithPath("result.retrospectDtoList[].contents[].answer").description("질문에 대한 답변"),
                                        fieldWithPath("result.retrospectDtoList[].keywords[].classify").description("회고 과정에서 키워드 분류 기준(감정 분리수거 기능)"),
                                        fieldWithPath("result.retrospectDtoList[].keywords[].keyword").description("분류된 키워드")
                                )
                        )
                );


    }
}