package com.nanal.backend.domain.diary.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class ReqGetCalendarDto {
    @Schema(description = "현재 날짜" , example = "0")
    @NotNull(message = "내용은 비어있을 수 없습니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime currentDate;

    @NotNull(message = "내용은 비어있을 수 없습니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime selectDate;
}
