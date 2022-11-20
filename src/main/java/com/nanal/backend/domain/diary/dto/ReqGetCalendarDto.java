package com.nanal.backend.domain.diary.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class ReqGetCalendarDto {

    @NotBlank(message = "currentDate 는 비어있을 수 없습니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime currentDate;

    @NotBlank(message = "selectDate 는 비어있을 수 없습니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime selectDate;
}
