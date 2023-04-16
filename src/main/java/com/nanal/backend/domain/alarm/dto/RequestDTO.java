package com.nanal.backend.domain.alarm.dto;

import lombok.Data;

@Data
public class RequestDTO {
    String targetToken;
    String title;
    String body;
}
