package com.nanal.backend.domain.alarm.dto;

import lombok.Data;

@Data
public class ReqUpdateRetrospectAlarm {

    private Boolean retrospectActive;

    private String retrospect;
}
