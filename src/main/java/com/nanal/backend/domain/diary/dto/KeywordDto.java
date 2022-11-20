package com.nanal.backend.domain.diary.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class KeywordDto {
    @Size(max = 5, message = "키워드는 최대 5개의 문자만 입력 가능합니다.")
    private String keyword;

    @Valid
    private List<KeywordEmotionDto> keywordEmotions;

}