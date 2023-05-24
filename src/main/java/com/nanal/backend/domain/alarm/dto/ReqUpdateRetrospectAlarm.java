package com.nanal.backend.domain.alarm.dto;

import lombok.Data;

@Data
public class ReqUpdateRetrospectAlarm {

    private Boolean retrospectAlarmActive;

    private String retrospectAlarmTime;
}
