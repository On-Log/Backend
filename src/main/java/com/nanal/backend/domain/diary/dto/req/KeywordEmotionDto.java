package com.nanal.backend.domain.diary.dto.req;

import lombok.Data;
import javax.validation.constraints.Size;

@Data
public class KeywordEmotionDto {
    @Size(max = 5, message = "emotion 은 최대 5개의 문자만 입력 가능합니다.")
    private String emotion;

}
