package com.nanal.backend.domain.diary.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReqGetDiaryDto {

    private LocalDateTime date;
}
