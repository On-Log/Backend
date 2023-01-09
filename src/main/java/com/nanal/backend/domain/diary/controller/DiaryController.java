package com.nanal.backend.domain.diary.controller;

import com.nanal.backend.domain.diary.dto.req.*;
import com.nanal.backend.domain.diary.dto.resp.RespGetCalendarDto;
import com.nanal.backend.domain.diary.dto.resp.RespGetDiaryDto;
import com.nanal.backend.domain.diary.dto.resp.RespGetEmotionDto;
import com.nanal.backend.domain.diary.service.DiaryService;
import com.nanal.backend.global.response.CommonResponse;
import com.nanal.backend.global.response.ErrorCode;
import com.nanal.backend.global.security.User;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RequiredArgsConstructor
@RestController
public class DiaryController {

    private final DiaryService diaryService;

    /**
     * 일기 탭 화면
     * [GET] /diary
     */
    @GetMapping("/diary")
    public CommonResponse<?> getCalendar(@AuthenticationPrincipal User user,
                                                          @Valid ReqGetCalendarDto reqGetCalendarDto) {

        // 요청 정보 기반으로 해당 날짜에 맞는 정보 조회
        RespGetCalendarDto respGetCalendarDto = diaryService.getCalendar(user.getSocialId(), reqGetCalendarDto);

        return new CommonResponse<>(respGetCalendarDto);
    }

    /**
     * 일기 기록
     * [POST] /diary
     */
    @PostMapping("/diary")
    public CommonResponse<?> saveDiary(@AuthenticationPrincipal User user,
                                       @RequestBody @Valid ReqSaveDiaryDto reqSaveDiaryDto) {

        // 요청 정보 기반으로 일기 저장
        diaryService.saveDiary(user.getSocialId(), reqSaveDiaryDto);

        return new CommonResponse<>(ErrorCode.SUCCESS);
    }

    /**
     * 일기 조회
     * [GET] /diary/view
     */
    @GetMapping("/diary/view")
    public CommonResponse<RespGetDiaryDto> getDiary(@AuthenticationPrincipal User user,
                                                    ReqGetDiaryDto reqGetDiaryDto) {

        // 요청 날짜 기반으로 일기 조회
        RespGetDiaryDto respGetDiaryDto = diaryService.getDiary(user.getSocialId(), reqGetDiaryDto);

        return new CommonResponse<>(respGetDiaryDto);
    }

    /**
     * 일기 수정
     * [PUT] /diary
     */
    @PutMapping("/diary")
    public CommonResponse<?> editDiary(@AuthenticationPrincipal User user,
                                       @RequestBody @Valid ReqEditDiaryDto reqEditDiary) {

        diaryService.editDiary(user.getSocialId(), reqEditDiary);

        return new CommonResponse<>(ErrorCode.SUCCESS);
    }

    /**
     * 일기 삭제
     * [PUT] /diary
     */
    @DeleteMapping("/diary")
    public CommonResponse<?> deleteDiary(@AuthenticationPrincipal User user,
                                         @RequestBody ReqDeleteDiaryDto reqDeleteDiaryDto) {

        System.out.println("test");
        System.out.println(user.getSocialId());
        System.out.println("드디어");
        diaryService.deleteDiary(user.getSocialId(), reqDeleteDiaryDto);

        return new CommonResponse<>(ErrorCode.SUCCESS);
    }
    /**
     * 감정어 조회
     * [GET] /diary/emotion
     */
    @GetMapping("/diary/emotion")
    public CommonResponse<RespGetEmotionDto> getEmotion() {

        // 감정어 조회
        RespGetEmotionDto respGetEmotionDto = diaryService.getEmotion();

        return new CommonResponse<>(respGetEmotionDto);
    }
}