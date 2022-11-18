package com.nanal.backend.domain.retrospect.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import java.time.LocalDateTime;

@Data
public class ReqEditRetroDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime editDate;

    @Max(value = 300, message="최대 300개의 문자만 입력 가능합니다.")
    private String answer;

    private int week;

    private int index;
}
