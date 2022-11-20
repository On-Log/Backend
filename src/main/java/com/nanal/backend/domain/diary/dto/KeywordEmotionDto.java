package com.nanal.backend.domain.diary.dto;

import lombok.Data;
import javax.validation.constraints.Size;

@Data
public class KeywordEmotionDto {
    @Size(max = 5, message = "감정어는 최대 5개의 문자만 입력 가능합니다.")
    private String emotion;

}
