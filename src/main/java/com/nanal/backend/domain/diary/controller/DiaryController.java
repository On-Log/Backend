package com.nanal.backend.domain.diary.controller;

import com.nanal.backend.config.response.CommonResponse;
import com.nanal.backend.config.response.ErrorCode;
import com.nanal.backend.domain.diary.dto.*;
import com.nanal.backend.domain.diary.service.DiaryService;
import com.nanal.backend.domain.oauth.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class DiaryController {

    private final DiaryService diaryService;

    /**
     * 일기 탭 화면
     * [GET] /diary
     * 작성자 : 장동호
     * 수정일 : 2022-11-09
     */
    @GetMapping("/diary")
    public CommonResponse<RespGetCalendarDto> getCalendar(@AuthenticationPrincipal UserDto userDto, ReqGetCalendarDto reqGetCalendarDto) {

        // 요청 정보 기반으로 해당 날짜에 맞는 정보 조회
        RespGetCalendarDto respGetCalendarDto = diaryService.getCalendar(userDto.getEmail(), reqGetCalendarDto);

        return new CommonResponse<>(respGetCalendarDto);
    }

    /**
     * 일기 기록
     * [POST] /diary
     * 작성자 : 장동호
     * 수정일 : 2022-11-09
     */
    @PostMapping("/diary")
    public CommonResponse<?> saveDiary(@AuthenticationPrincipal UserDto userDto, @RequestBody ReqSaveDiaryDto reqSaveDiaryDto) {

        // 요청 정보 기반으로 일기 저장
        diaryService.saveDiary(userDto.getEmail(), reqSaveDiaryDto);

        return new CommonResponse<>(ErrorCode.SUCCESS);
    }

    /**
     * 일기 조회
     * [GET] /diary/view
     * 작성자 : 장동호
     * 수정일 : 2022-11-10
     */
    @GetMapping("/diary/view")
    public CommonResponse<?> getDiary(@AuthenticationPrincipal UserDto userDto, ReqGetDiaryDto reqGetDiaryDto) {

        // 요청 날짜 기반으로 일기 조회
        RespGetDiaryDto respGetDiaryDto = diaryService.getDiary(userDto.getEmail(), reqGetDiaryDto);

        return new CommonResponse<>(respGetDiaryDto);
    }

    /**
     * 일기 수정
     * [PUT] /diary/view
     * 작성자 : 장동호
     * 수정일 :
     */
    @PutMapping("/diary")
    public void editDiary() {

    }

    /**
     * 감정어 조회
     * [GET] /diary/emotion
     * 작성자 : 장동호
     * 수정일 : 2022-11-09
     */
    @GetMapping("/diary/emotion")
    public CommonResponse<RespGetEmotionDto> getEmotion() {

        // 감정어 조회
        RespGetEmotionDto respGetEmotionDto = diaryService.getEmotion();

        return new CommonResponse<>(respGetEmotionDto);
    }
}
