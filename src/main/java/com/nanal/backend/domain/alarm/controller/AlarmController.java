package com.nanal.backend.domain.alarm.controller;

import com.nanal.backend.domain.alarm.dto.ReqUpdateDiaryAlarm;
import com.nanal.backend.domain.alarm.dto.ReqUpdateRetrospectAlarm;
import com.nanal.backend.domain.alarm.service.AlarmService;
import com.nanal.backend.global.response.CommonResponse;
import com.nanal.backend.global.response.ErrorCode;
import com.nanal.backend.global.security.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class AlarmController {

    private final AlarmService alarmService;

    @PutMapping("/alarm/diary")
    public CommonResponse<?> updateDiaryAlarm(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid ReqUpdateDiaryAlarm reqUpdateDiaryAlarm) {

        alarmService.updateDiaryAlarm(user.getSocialId(), reqUpdateDiaryAlarm);

        return new CommonResponse<>(ErrorCode.SUCCESS);
    }

    @PutMapping("/alarm/retrospect")
    public CommonResponse<?> updateRetrospectAlarm(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid ReqUpdateRetrospectAlarm reqUpdateRetrospectAlarm) {

        alarmService.updateRetrospectAlarm(user.getSocialId(), reqUpdateRetrospectAlarm);

        return new CommonResponse<>(ErrorCode.SUCCESS);
    }
}
