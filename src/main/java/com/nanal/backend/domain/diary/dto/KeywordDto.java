package com.nanal.backend.domain.diary.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class KeywordDto {
    @Size(max = 5, message = "최대 5개의 문자만 입력 가능합니다.")
    private String keyword;

    @NotNull(message = "내용은 비어있을 수 없습니다.")
    private List<KeywordEmotionDto> keywordEmotions;

}