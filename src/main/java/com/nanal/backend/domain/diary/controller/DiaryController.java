package com.nanal.backend.domain.diary.controller;

import com.nanal.backend.config.response.CommonResponse;
import com.nanal.backend.config.response.ErrorCode;
import com.nanal.backend.domain.diary.dto.ReqSaveDiaryDto;
import com.nanal.backend.domain.diary.service.DiaryService;
import com.nanal.backend.domain.oauth.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class DiaryController {

    private final DiaryService diaryService;

    /**
     * 일기 탭 화면
     * [GET] /diary
     * 작성자 : 장동호
     * 수정일 :
     */
    @GetMapping("/diary")
    public void getCalendar() {

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
     * 수정일 :
     */
    @GetMapping("/diary/view")
    public void getDiary() {

    }

    /**
     * 감정어 조회
     * [GET] /diary/emotion
     * 작성자 : 장동호
     * 수정일 :
     */
    @GetMapping("/diary/emotion")
    public void getEmotion() {

    }
}
