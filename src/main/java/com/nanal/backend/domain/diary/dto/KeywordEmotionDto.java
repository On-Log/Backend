package com.nanal.backend.domain.diary.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class KeywordEmotionDto {
    @Size(max = 5, message = "최대 5개의 문자만 입력 가능합니다.")
    private String emotion;

}
