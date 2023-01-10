package com.nanal.backend.domain.retrospect.controller;

import com.nanal.backend.config.CommonControllerTest;
import com.nanal.backend.domain.diary.dto.req.KeywordEmotionDto;
import com.nanal.backend.domain.retrospect.dto.QuestionsDto;
import com.nanal.backend.domain.retrospect.dto.RetrospectContentDto;
import com.nanal.backend.domain.retrospect.dto.RetrospectKeywordDto;
import com.nanal.backend.domain.retrospect.dto.req.KeywordDto;
import com.nanal.backend.domain.retrospect.dto.req.ReqEditRetroDto;
import com.nanal.backend.domain.retrospect.dto.req.ReqSaveRetroDto;
import com.nanal.backend.domain.retrospect.dto.resp.*;
import com.nanal.backend.domain.retrospect.service.RetrospectService;
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

    @Test
    public void 회고_저장() throws Exception {
        //given
        List<RetrospectContentDto> retrospectContentDtos = new ArrayList<>(Arrays.asList(new RetrospectContentDto("이번주 나의 모습은 어땠나요?", "답변1"),
                new RetrospectContentDto("다른 내 모습도 들려줄래요? 이번주에 찾은 의외의 내 모습이 있다면요?", "답변2"), new RetrospectContentDto("다음주에도 유지하고 싶은 나의 모습이 있을까요? 혹은 새롭게 찾고 싶은 나의 모습이 있다면 무엇인가요?", "답변3")));
        List<RetrospectKeywordDto> retrospectKeywordDtos = new ArrayList<>(Arrays.asList(new RetrospectKeywordDto("그때 그대로 의미있었던 행복한 기억", "키워드1"),
                new RetrospectKeywordDto("나를 힘들게 했지만 도움이 된 기억", "키워드2"),
                new RetrospectKeywordDto("돌아보니, 다른 의미로 다가온 기억", "키워드3")));
        ReqSaveRetroDto reqSaveRetroDto = new ReqSaveRetroDto(LocalDateTime.parse("2023-01-24T00:00:00"), "자아탐색", retrospectContentDtos, retrospectKeywordDtos);

        willDoNothing().given(retrospectService).saveRetrospect(any(), any()); //void일때는 willDoNothing 사용

        //when
        ResultActions actions = mockMvc.perform(
                post("/retrospect")
                        .header("Token", "ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqSaveRetroDto))
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
                                        fieldWithPath("currentDate").description("회고 저장 날짜"),
                                        fieldWithPath("goal").description("회고 목적"),
                                        fieldWithPath("contents[].question").description("회고 목적별 질문"),
                                        fieldWithPath("contents[].answer").description("질문에 대한 답변"),
                                        fieldWithPath("keywords[].classify").description("회고 과정에서 키워드 분류 기준(감정 분리수거 기능)"),
                                        fieldWithPath("keywords[].keyword").description("분류된 키워드")
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
    public void 회고_조회() throws Exception {
        //given
        String selectDate = "2023-01-24T00:00:00";
        int week = 0;
        List<RetrospectContentDto> retrospectContentDtos = new ArrayList<>(Arrays.asList(new RetrospectContentDto("이번주 나의 모습은 어땠나요?", "답변1"),
                new RetrospectContentDto("다른 내 모습도 들려줄래요? 이번주에 찾은 의외의 내 모습이 있다면요?", "답변2"), new RetrospectContentDto("다음주에도 유지하고 싶은 나의 모습이 있을까요? 혹은 새롭게 찾고 싶은 나의 모습이 있다면 무엇인가요?", "답변3")));
        List<RetrospectKeywordDto> retrospectKeywordDtos = new ArrayList<>(Arrays.asList(new RetrospectKeywordDto("그때 그대로 의미있었던 행복한 기억", "키워드1"),
                new RetrospectKeywordDto("나를 힘들게 했지만 도움이 된 기억", "키워드2"),
                new RetrospectKeywordDto("돌아보니, 다른 의미로 다가온 기억", "키워드3")));
        RespGetRetroDto respGetRetroDto = new RespGetRetroDto(LocalDateTime.parse("2023-01-18T00:00:00"), retrospectContentDtos, retrospectKeywordDtos);
        given(retrospectService.getRetro(any(), any())).willReturn(respGetRetroDto);

        //when
        ResultActions actions = mockMvc.perform(
                get("/retrospect/view")
                        .header("Token", "ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("selectDate", selectDate)
                        .param("week", String.valueOf(week))
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
                                        parameterWithName("selectDate").description("선택 날짜"),
                                        parameterWithName("week").description("회고 주차 (index로 되어있어서 1주 차는 0, 2주 차는 1 이런 식으로 보내야 함)")
                                ),
                                responseFields(
                                        fieldWithPath("isSuccess").description("성공 여부"),
                                        fieldWithPath("code").description("상태 코드"),
                                        fieldWithPath("message").description("결과 메시지"),
                                        fieldWithPath("result.writeDate").description("회고 작성 날짜"),
                                        fieldWithPath("result.contents[].question").description("회고 목적별 질문"),
                                        fieldWithPath("result.contents[].answer").description("질문에 대한 답변"),
                                        fieldWithPath("result.keywords[].classify").description("회고 과정에서 키워드 분류 기준(감정 분리수거 기능)"),
                                        fieldWithPath("result.keywords[].keyword").description("분류된 키워드")
                                )
                        )
                );
    }

    @Test
    public void 회고_수정() throws Exception {
        //given
        ReqEditRetroDto reqEditRetroDto = new ReqEditRetroDto(LocalDateTime.parse("2023-01-24T00:00:00"), "수정답변", 0, 0);
        willDoNothing().given(retrospectService).editRetrospect(any(), any());

        //when
        ResultActions actions = mockMvc.perform(
                put("/retrospect")
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
                                        fieldWithPath("editDate").description("회고 수정 날짜"),
                                        fieldWithPath("answer").description("수정된 회고 질문에 대한 답변"),
                                        fieldWithPath("week").description("회고 주차 (index로 되어있어서 1주 차는 0, 2주 차는 1 이런 식으로 보내야 함)"),
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
    public void 일기_키워드_감정어_조회() throws Exception{
        //given
        String currentDate = "2023-01-24T00:00:00";
        List<KeywordEmotionDto> keywordEmotionDtoList = new ArrayList<>(Arrays.asList(
                new KeywordEmotionDto("아쉬움"),
                new KeywordEmotionDto("복잡"),
                new KeywordEmotionDto("기대")
        ));

        List<KeywordDto> keywordDtoList = new ArrayList<>(Arrays.asList(
                new KeywordDto("창업", keywordEmotionDtoList),
                new KeywordDto("취업", keywordEmotionDtoList),
                new KeywordDto("막학기", keywordEmotionDtoList)
        ));

        List<KeywordWriteDateDto> keywordWriteDateDtos = new ArrayList<>(Arrays.asList(new KeywordWriteDateDto(LocalDateTime.parse("2023-01-21T00:00:00"),keywordDtoList)));
        RespGetKeywordAndEmotionDto respGetKeywordAndEmotionDto = new RespGetKeywordAndEmotionDto(keywordWriteDateDtos);
        given(retrospectService.getKeywordAndEmotion(any(), any())).willReturn(respGetKeywordAndEmotionDto);

        //when
        ResultActions actions = mockMvc.perform(
                get("/retrospect/keyword")
                        .header("Token", "ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("currentDate", currentDate)
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
                                        parameterWithName("currentDate").description("현재 날짜")
                                ),
                                responseFields(
                                        fieldWithPath("isSuccess").description("성공 여부"),
                                        fieldWithPath("code").description("상태 코드"),
                                        fieldWithPath("message").description("결과 메시지"),
                                        fieldWithPath("result.keywords[].writeDate").description("일기 작성 날짜"),
                                        fieldWithPath("result.keywords[].keywords[].keyword").description("해당 날짜에 작성한 일기 키워드"),
                                        fieldWithPath("result.keywords[].keywords[].keywordEmotions[].emotion").description("키워드에 해당하는 감정어")
                                )
                        )
                );

    }

    @Test
    public void 회고질문_도움말_조회() throws Exception {
        //given
        int goalIndex = 1;
        List<QuestionsDto> questionsDtos = new ArrayList<>(Arrays.asList(new QuestionsDto("이번주 나의 모습은 어땠나요?", "이번주 나의 모습을 묘사하기 어려우신가요? 가장 먼저 떠오르는 내 모습, 혹은 가장 자주 보였던 나의 모습을 떠올려보세요."),
                new QuestionsDto("다른 내 모습도 들려줄래요? 이번주에 찾은 의외의 내 모습이 있다면요?", "우리의 일주일은 한가지 색만으로 이루어져있지 않아요! 가장 사소한 일부터 차근 차근 생각해보세요."),
                new QuestionsDto("다음주에도 유지하고 싶은 나의 모습이 있을까요? 혹은 새롭게 찾고 싶은 나의 모습이 있다면 무엇인가요?", "그 모습의 나는 구체적으로 어떤 행동을 하게 될까요? 그 모습이 되려면 어떤 노력을 해야할지도 생각해봅시다.")));
        RespGetQuestionAndHelpDto respGetQuestionAndHelpDto = new RespGetQuestionAndHelpDto(questionsDtos);
        given(retrospectService.getQuestionAndHelp(any())).willReturn(respGetQuestionAndHelpDto);

        //when
        ResultActions actions = mockMvc.perform(
                get("/retrospect/question")
                        .header("Token", "ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("goalIndex", String.valueOf(goalIndex))
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
                                        parameterWithName("goalIndex").description("선택한 회고 목적 1.자아탐색 2.성취확인 3.감정정리 4.관계고민")
                                ),
                                responseFields(
                                        fieldWithPath("isSuccess").description("성공 여부"),
                                        fieldWithPath("code").description("상태 코드"),
                                        fieldWithPath("message").description("결과 메시지"),
                                        fieldWithPath("result.questionAndHelp[].question").description("회고 목적에 대한 질문"),
                                        fieldWithPath("result.questionAndHelp[].help").description("질문에 대한 도움말")
                                )
                        )
                );
    }

}
