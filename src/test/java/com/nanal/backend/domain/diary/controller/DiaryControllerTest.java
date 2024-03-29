package com.nanal.backend.domain.diary.controller;

import com.nanal.backend.config.CommonControllerTest;
import com.nanal.backend.domain.diary.dto.req.*;
import com.nanal.backend.domain.diary.dto.resp.RespGetCalendarDto;
import com.nanal.backend.domain.diary.dto.resp.RespGetDiaryDto;
import com.nanal.backend.domain.diary.dto.resp.RespGetEmotionDto;
import com.nanal.backend.domain.diary.dto.resp.RetrospectInfoDto;
import com.nanal.backend.domain.diary.service.DiaryService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DiaryController.class)
class DiaryControllerTest extends CommonControllerTest {

    @MockBean
    private DiaryService diaryService;

    @Test
    public void 일기_탭() throws Exception {
        //given
        String fromDate = "2023-01-01T00:00:00";
        String toDate = "2023-01-31T00:00:00";

        List<LocalDateTime> existDiaryDate = new ArrayList<>(Arrays.asList(
                LocalDateTime.parse("2023-01-01T00:00:00"),
                LocalDateTime.parse("2023-01-03T00:00:00"),
                LocalDateTime.parse("2023-01-09T00:00:00"),
                LocalDateTime.parse("2023-01-13T00:00:00"),
                LocalDateTime.parse("2023-01-16T00:00:00"))
        );

        List<RetrospectInfoDto> retrospectInfoDtoList = new ArrayList<>(Arrays.asList(
                new RetrospectInfoDto(LocalDateTime.parse("2023-01-03T00:00:00"), "자아탐색"),
                new RetrospectInfoDto(LocalDateTime.parse("2023-01-10T00:00:00"), "성취확인")
        ));

        RespGetCalendarDto output = new RespGetCalendarDto(
                "nickname",
                false,
                true,
                existDiaryDate,
                LocalDateTime.parse("2023-01-18T00:00:00"),
                LocalDateTime.parse("2023-01-24T00:00:00"),
                retrospectInfoDtoList
        );

        given(diaryService.getCalendar(any(), any())).willReturn(output);

        //when
        ResultActions actions = mockMvc.perform(
                get("/diary")
                        .header("Token", "ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
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
                                        fieldWithPath("result.existRetrospect").description("이번주 회고의 작성 여부"),
                                        fieldWithPath("result.isRetrospectDay").description("오늘이 회고일인지 체크"),
                                        fieldWithPath("result.existDiaryDate").description("일기 존재 날짜"),
                                        fieldWithPath("result.nextDayOfPrevRetroDate").description("이전 회고일의 다음일"),
                                        fieldWithPath("result.retroDate").description("이번주 회고일"),
                                        fieldWithPath("result.retrospectInfoList[].retrospectDate").description("회고 작성일"),
                                        fieldWithPath("result.retrospectInfoList[].goal").description("회고 목적")
                                )
                        )
                );
    }

    @Test
    public void 일기_저장() throws Exception {
        //given
        String saveDate = "2023-01-22T00:00:00";

        List<KeywordEmotionDto> keywordEmotionDtoList = new ArrayList<>(Arrays.asList(
                new KeywordEmotionDto("행복"),
                new KeywordEmotionDto("안심"),
                new KeywordEmotionDto("의욕")
        ));
        List<KeywordDto> keywordDtoList = new ArrayList<>(Arrays.asList(
                new KeywordDto("창업", keywordEmotionDtoList),
                new KeywordDto("취업", keywordEmotionDtoList),
                new KeywordDto("막학기", keywordEmotionDtoList)
        ));

        ReqDiaryDto input = ReqDiaryDto.builder()
                .date(LocalDateTime.parse(saveDate))
                .content("마지막 방학... 계절 학기 언제 끝나...")
                .keywords(keywordDtoList)
                .build();

        String body = objectMapper.writeValueAsString(input);

        willDoNothing().given(diaryService).writeDiary(any(), any());

        //when
        ResultActions actions = mockMvc.perform(
                post("/diary")
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
                                        fieldWithPath("date").description("작성 날짜"),
                                        fieldWithPath("content").description("일기 내용"),
                                        fieldWithPath("keywords[].keyword").description("키워드"),
                                        fieldWithPath("keywords[].keywordEmotions[].emotion").description("감정어")
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
    public void 일기_조회() throws Exception {
        //given
        String date = "2023-01-15T00:00:00";

        List<KeywordEmotionDto> keywordEmotionDtoList = new ArrayList<>(Arrays.asList(
                new KeywordEmotionDto("놀람"),
                new KeywordEmotionDto("힘듦"),
                new KeywordEmotionDto("상쾌")
        ));
        List<KeywordDto> keywordDtoList = new ArrayList<>(Arrays.asList(
                new KeywordDto("창업", keywordEmotionDtoList),
                new KeywordDto("취업", keywordEmotionDtoList),
                new KeywordDto("막학기", keywordEmotionDtoList)
        ));

        RespGetDiaryDto output = RespGetDiaryDto.builder()
                .writeDate(LocalDateTime.parse(date))
                .content("마지막 방학... 계절 학기 언제 끝나...")
                .keywords(keywordDtoList)
                .editStatus(true)
                .build();

        given(diaryService.getDiary(any(), any())).willReturn(output);

        //when
        ResultActions actions = mockMvc.perform(
                get("/diary/view")
                        .header("Token", "ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("date" ,date)
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
                                        parameterWithName("date").description("작성 날짜")
                                ),
                                responseFields(
                                        fieldWithPath("isSuccess").description("성공 여부"),
                                        fieldWithPath("code").description("상태 코드"),
                                        fieldWithPath("message").description("결과 메시지"),
                                        fieldWithPath("result.writeDate").description("작성 날짜"),
                                        fieldWithPath("result.content").description("일기 내용"),
                                        fieldWithPath("result.keywords[].keyword").description("키워드"),
                                        fieldWithPath("result.keywords[].keywordEmotions[].emotion").description("감정어"),
                                        fieldWithPath("result.editStatus").description("일기 수정 가능한지 여부. true면 수정 가능")
                                )
                        )
                );
    }

    @Test
    public void 일기_수정() throws Exception {
        //given
        String editDate = "2023-01-15T00:00:00";

        List<KeywordEmotionDto> keywordEmotionDtoList = new ArrayList<>(Arrays.asList(
                new KeywordEmotionDto("즐거움"),
                new KeywordEmotionDto("여유"),
                new KeywordEmotionDto("부담")
        ));
        List<KeywordDto> keywordDtoList = new ArrayList<>(Arrays.asList(
                new KeywordDto("방학", keywordEmotionDtoList),
                new KeywordDto("나날", keywordEmotionDtoList),
                new KeywordDto("여행", keywordEmotionDtoList)
        ));

        ReqDiaryDto input = ReqDiaryDto.builder()
                .date(LocalDateTime.parse(editDate))
                .content("이제 마지막 학기네요...")
                .keywords(keywordDtoList)
                .build();

        String body = objectMapper.writeValueAsString(input);

        willDoNothing().given(diaryService).updateDiary(any(), any());

        //when
        ResultActions actions = mockMvc.perform(
                put("/diary")
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
                                        fieldWithPath("date").description("수정 날짜"),
                                        fieldWithPath("content").description("일기 내용"),
                                        fieldWithPath("keywords[].keyword").description("키워드"),
                                        fieldWithPath("keywords[].keywordEmotions[].emotion").description("감정어")
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
    public void 일기_삭제() throws Exception {
        //given
        String deleteDate = "2023-01-15T00:00:00";

        ReqDeleteDiaryDto input = new ReqDeleteDiaryDto(LocalDateTime.parse(deleteDate));
        String body = objectMapper.writeValueAsString(input);

        willDoNothing().given(diaryService).deleteDiary(any(), any());

        //when
        ResultActions actions = mockMvc.perform(
                delete("/diary")
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
                                        fieldWithPath("date").description("삭제 날짜")
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
    public void 감정어_조회() throws Exception {
        //given
        RespGetEmotionDto output = new RespGetEmotionDto(new ArrayList<>(Arrays.asList("행복", "슬픔")));
        given(diaryService.getEmotion()).willReturn(output);

        //when
        ResultActions actions = mockMvc.perform(
                get("/diary/emotion")
                        .header("Token", "ACCESS_TOKEN"));

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
                                        fieldWithPath("result.emotion").description("감정어")
                                )
                        )
                );
    }
}