package com.nanal.backend.domain.diary.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReqEditDiaryDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime editDate;

    private String content;

    private List<KeywordDto> keywords;
}
