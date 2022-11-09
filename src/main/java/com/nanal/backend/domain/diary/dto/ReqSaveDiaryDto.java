package com.nanal.backend.domain.diary.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReqSaveDiaryDto {

    private LocalDateTime date;

    private String content;

    private List<KeywordDto> keywords;

}
