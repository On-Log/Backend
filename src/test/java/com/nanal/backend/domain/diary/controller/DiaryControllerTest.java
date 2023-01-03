package com.nanal.backend.domain.diary.controller;

import com.nanal.backend.domain.CommonControllerTest;
import com.nanal.backend.domain.diary.dto.resp.RespGetEmotionDto;
import com.nanal.backend.domain.diary.service.DiaryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//@WebMvcTest(DiaryController.class)
class DiaryControllerTest extends CommonControllerTest {

    @MockBean
    private DiaryService diaryService;

    @Autowired
    public MockMvc mockMvc;

    @Test
    public void 감정어_조회() throws Exception {
        //given
        RespGetEmotionDto respGetEmotionDto = new RespGetEmotionDto(new ArrayList<>(Arrays.asList("행복", "슬픔")));
        given(diaryService.getEmotion()).willReturn(respGetEmotionDto);

        //when
        ResultActions actions = mockMvc.perform(
                get("/diary/emotion"));

        //then
        actions
                .andExpect(status().isOk())
                .andDo(document("docs-test",
                        preprocessRequest(prettyPrint()),
                        responseFields(
                                fieldWithPath("emotion").description("감정")
                        ))
                );
    }
}