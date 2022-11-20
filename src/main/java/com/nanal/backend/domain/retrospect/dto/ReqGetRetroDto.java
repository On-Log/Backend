package com.nanal.backend.domain.retrospect.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class ReqGetRetroDto {
    //현재 시간
    @Schema(description = "현재 날짜 및 시간" , example = "2022-11-19T05:33:42.387Z")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime currentDate;

    //선택한 월
    @Schema(description = "" , example = "2022-10-17T05:33:42.387Z")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime selectDate;

    @Schema(description = "week" , example = "3")
    private int week;
}
