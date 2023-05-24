package com.nanal.backend.domain.alarm.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class ReqUpdateDiaryAlarm {

    @NotNull(message = "diaryAlarmActive 값이 올바르지 않습니다.")
    private Boolean diaryAlarmActive;

    @NotBlank(message = "diaryAlarmTime 는 비어있을 수 없습니다.")
    @Pattern(message = "diaryAlarmTime 값이 올바르지 않습니다.",
            regexp = "^(0[0-9]|1[0-9]|2[0-3]):([0-5][0-9])$")
    private String diaryAlarmTime;
}
