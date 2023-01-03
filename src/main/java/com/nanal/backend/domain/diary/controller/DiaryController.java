package com.nanal.backend.domain.diary.controller;

import com.nanal.backend.domain.diary.dto.req.*;
import com.nanal.backend.domain.diary.dto.resp.RespGetCalendarDto;
import com.nanal.backend.domain.diary.dto.resp.RespGetDiaryDto;
import com.nanal.backend.domain.diary.dto.resp.RespGetEmotionDto;
import com.nanal.backend.domain.diary.service.DiaryService;
import com.nanal.backend.global.response.CommonResponse;
import com.nanal.backend.global.response.ErrorCode;
import com.nanal.backend.global.security.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RequiredArgsConstructor
@RestController
@Tag(name = "DiaryController", description = "일기 관련 api")

public class DiaryController {

    private final DiaryService diaryService;

    /**
     * 일기 탭 화면
     * [GET] /diary
     * 작성자 : 장동호
     * 수정일 : 2022-11-18
     */
    @GetMapping("/diary")
    public CommonResponse<RespGetCalendarDto> getCalendar(@Parameter(hidden = true) @AuthenticationPrincipal UserDto userDto,
                                                          @Valid ReqGetCalendarDto reqGetCalendarDto) {

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
    public CommonResponse<?> saveDiary(@Parameter(hidden = true) @AuthenticationPrincipal UserDto userDto,
                                       @RequestBody @Valid ReqSaveDiaryDto reqSaveDiaryDto) {

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
    public CommonResponse<RespGetDiaryDto> getDiary(@Parameter(hidden = true) @AuthenticationPrincipal UserDto userDto,
                                                    ReqGetDiaryDto reqGetDiaryDto) {

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
    public CommonResponse<?> editDiary(@Parameter(hidden = true) @AuthenticationPrincipal UserDto userDto,
                                       @RequestBody @Valid ReqEditDiaryDto reqEditDiary) {

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
    public CommonResponse<?> deleteDiary(@Parameter(hidden = true) @AuthenticationPrincipal UserDto userDto,
                                         ReqDeleteDiaryDto reqDeleteDiaryDto) {

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
    public CommonResponse<RespGetEmotionDto> getEmotion(@Parameter(hidden = true) @AuthenticationPrincipal UserDto userDto) {

        // 감정어 조회
        RespGetEmotionDto respGetEmotionDto = diaryService.getEmotion();

        return new CommonResponse<>(respGetEmotionDto);
    }
}
