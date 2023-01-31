package com.nanal.backend.domain.diary.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class KeywordEmotionDto {
    @Size(min = 1, max = 5, message = "emotion 은 최소 1개, 최대 5개의 문자만 입력 가능합니다.")
    private String emotion;

}
