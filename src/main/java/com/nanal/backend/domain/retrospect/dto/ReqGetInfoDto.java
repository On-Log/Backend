package com.nanal.backend.domain.retrospect.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReqGetInfoDto {

    @Schema(description = "현재 날짜" , example = "2022-11-19T05:33:42.387Z")
    private LocalDateTime currentDate;

    @Schema(description = "선택된 날짜" , example = "2022-11-13T05:33:42.387Z")
    private LocalDateTime selectDate;
}
