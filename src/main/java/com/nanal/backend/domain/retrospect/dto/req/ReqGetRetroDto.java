package com.nanal.backend.domain.retrospect.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class ReqGetRetroDto {

    //선택한 월
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(description = "" , example = "2022-10-17T05:33:42.387Z")
    private LocalDateTime selectDate;

    @Schema(description = "week" , example = "3")
    private int week;
}
