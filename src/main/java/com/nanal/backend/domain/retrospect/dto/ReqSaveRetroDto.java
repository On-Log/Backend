package com.nanal.backend.domain.retrospect.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class ReqSaveRetroDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;

    private String goal;

    private List<RetrospectContentDto> contents;

    private List<RetrospectKeywordDto> keywords;
}
