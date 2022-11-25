package com.nanal.backend.domain.retrospect.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class ReqGetKeywordAndEmotionDto {

    //현재 시간
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(description = "현재 날짜 및 시간" , example = "2022-11-19T05:33:42.387Z")
    private LocalDateTime currentDate;
}
