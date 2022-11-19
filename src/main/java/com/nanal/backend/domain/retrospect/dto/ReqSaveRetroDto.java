package com.nanal.backend.domain.retrospect.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
@Data
public class ReqSaveRetroDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;

    @Schema(description = "목표" , example = "1등하기")
    @NotNull(message = "목표는 비어있을 수 없습니다.")
    @Size(max = 30, message="최대 30개의 문자만 입력 가능합니다.")
    private String goal;

    private List<RetrospectContentDto> contents;
    private List<RetrospectKeywordDto> keywords;
}
