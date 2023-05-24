package com.nanal.backend.domain.alarm.dto;

import lombok.Data;

@Data
public class ReqUpdateDiaryAlarm {

    private Boolean diaryAlarmActive;

    private String diaryAlarmTime;
}
