package com.nanal.backend.domain.retrospect.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
@Data
public class ReqSaveRetroDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;
    @NotNull(message = "목표는 비어있을 수 없습니다.")
    @Max(value = 30, message="최대 30개의 문자만 입력 가능합니다.")
    private String goal;

    private List<RetrospectContentDto> contents;

    private List<RetrospectKeywordDto> keywords;
}
