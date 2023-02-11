package com.nanal.backend.domain.retrospect.dto.req;

import com.nanal.backend.domain.diary.dto.req.KeywordEmotionDto;
import com.nanal.backend.domain.diary.entity.Keyword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class KeywordDto {
    //키워드 구분하기 위한 specifyKey
    private String specifyKey;

    @Size(min = 1, max = 5, message = "keyword 는 최소 1개, 최대 5개의 문자만 입력 가능합니다.")
    private String keyword;

    @Valid
    private List<KeywordEmotionDto> keywordEmotions;

    public static List<KeywordEmotionDto> makeKeywordDtoList(Keyword keyword) {
        return keyword.getKeywordEmotions().stream()
                .map(ke -> new KeywordEmotionDto(ke.getEmotion().getEmotion()))
                .collect(Collectors.toList());
    }
}