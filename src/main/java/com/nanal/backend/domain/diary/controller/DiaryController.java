package com.nanal.backend.domain.diary.controller;

import com.nanal.backend.global.response.CommonResponse;
import com.nanal.backend.global.response.ErrorCode;
import com.nanal.backend.domain.diary.dto.*;
import com.nanal.backend.domain.diary.service.DiaryService;
import com.nanal.backend.global.auth.oauth.UserDto;
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
     * 작성자 : 장동호
     * 수정일 : 2022-11-18
     */
    @GetMapping("/diary")
    public CommonResponse<RespGetCalendarDto> getCalendar(@AuthenticationPrincipal UserDto userDto, @Valid ReqGetCalendarDto reqGetCalendarDto) {

        // 요청 정보 기반으로 해당 날짜에 맞는 정보 조회
        RespGetCalendarDto respGetCalendarDto = diaryService.getCalendar(userDto.getEmail(), reqGetCalendarDto);

        return new CommonResponse<>(respGetCalendarDto);
    }

    /**
     * 일기 기록
     * [POST] /diary
     * 작성자 : 장동호
     * 수정일 : 2022-11-18
     */
    @PostMapping("/diary")
    public CommonResponse<?> saveDiary(@AuthenticationPrincipal UserDto userDto, @RequestBody @Valid ReqSaveDiaryDto reqSaveDiaryDto) {

        // 요청 정보 기반으로 일기 저장
        diaryService.saveDiary(userDto.getEmail(), reqSaveDiaryDto);

        return new CommonResponse<>(ErrorCode.SUCCESS);
    }

    /**
     * 일기 조회
     * [GET] /diary/view
     * 작성자 : 장동호
     * 수정일 : 2022-11-18
     */
    @GetMapping("/diary/view")
    public CommonResponse<RespGetDiaryDto> getDiary(@AuthenticationPrincipal UserDto userDto, ReqGetDiaryDto reqGetDiaryDto) {

        // 요청 날짜 기반으로 일기 조회
        RespGetDiaryDto respGetDiaryDto = diaryService.getDiary(userDto.getEmail(), reqGetDiaryDto);

        return new CommonResponse<>(respGetDiaryDto);
    }

    /**
     * 일기 수정
     * [PUT] /diary
     * 작성자 : 장동호
     * 수정일 : 2022-11-18
     */
    @PutMapping("/diary")
    public CommonResponse<?> editDiary(@AuthenticationPrincipal UserDto userDto, @RequestBody @Valid ReqEditDiaryDto reqEditDiary) {

        diaryService.editDiary(userDto.getEmail(), reqEditDiary);

        return new CommonResponse<>(ErrorCode.SUCCESS);
    }

    /**
     * 일기 삭제
     * [PUT] /diary
     * 작성자 : 장동호
     * 수정일 : 2022-11-18
     */
    @DeleteMapping("/diary")
    public CommonResponse<?> deleteDiary(@AuthenticationPrincipal UserDto userDto, ReqDeleteDiaryDto reqDeleteDiaryDto) {

        diaryService.deleteDiary(userDto.getEmail(), reqDeleteDiaryDto);

        return new CommonResponse<>(ErrorCode.SUCCESS);
    }
    /**
     * 감정어 조회
     * [GET] /diary/emotion
     * 작성자 : 장동호
     * 수정일 : 2022-11-18
     */
    @GetMapping("/diary/emotion")
    public CommonResponse<RespGetEmotionDto> getEmotion(@AuthenticationPrincipal UserDto userDto) {

        // 감정어 조회
        RespGetEmotionDto respGetEmotionDto = diaryService.getEmotion();

        return new CommonResponse<>(respGetEmotionDto);
    }
}
