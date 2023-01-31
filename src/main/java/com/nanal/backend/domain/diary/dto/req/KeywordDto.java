package com.nanal.backend.domain.diary.dto.req;

import com.nanal.backend.domain.diary.entity.Keyword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class KeywordDto {
    @Size(min = 1, max = 5, message = "keyword 는 최소 1개, 최대 5개의 문자만 입력 가능합니다.")
    private String keyword;

    @Valid
    @Size(min = 3, max = 3, message ="emotion 의 개수는 3개여야 합니다.")
    private List<KeywordEmotionDto> keywordEmotions;

    public KeywordDto(Keyword keyword) {
        this.keyword = keyword.getWord();

        List<KeywordEmotionDto> keywordEmotionDtos = new ArrayList<>();
        keywordEmotionDtos.add(new KeywordEmotionDto(keyword.getEmotionList().getFirstEmotion()));
        keywordEmotionDtos.add(new KeywordEmotionDto(keyword.getEmotionList().getSecondEmotion()));
        keywordEmotionDtos.add(new KeywordEmotionDto(keyword.getEmotionList().getThirdEmotion()));

        this.keywordEmotions = keywordEmotionDtos;
    }
}