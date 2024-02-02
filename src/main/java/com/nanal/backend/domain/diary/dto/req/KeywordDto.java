package com.nanal.backend.domain.diary.dto.req;

import com.nanal.backend.domain.diary.entity.Keyword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class KeywordDto {
    @NotNull(message = "keyword 는 비어있을 수 없습니다.")
    @Size(min = 1, max = 7, message = "keyword 는 최소 1개, 최대 7개의 문자만 입력 가능합니다.")
    private String keyword;

    @Valid
    @Size(min = 1, max = 3, message ="emotion 의 개수는 3개여야 합니다.")
    private List<KeywordEmotionDto> keywordEmotions;

    public KeywordDto(Keyword keyword) {
        this.keyword = keyword.getWord();
        this.keywordEmotions = keyword.getKeywordEmotions().stream()
                            .map(ke -> new KeywordEmotionDto(ke.getEmotion().getEmotion()))
                            .collect(Collectors.toList());
    }
}
