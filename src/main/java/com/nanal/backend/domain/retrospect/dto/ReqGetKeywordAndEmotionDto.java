package com.nanal.backend.domain.retrospect.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReqGetKeywordAndEmotionDto {

    //현재 시간
    @Schema(description = "현재 날짜 및 시간" , example = "2022-11-19T05:33:42.387Z")
    private LocalDateTime currentDate;
}
