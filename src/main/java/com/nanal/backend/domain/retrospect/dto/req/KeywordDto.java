package com.nanal.backend.domain.retrospect.dto.req;

import com.nanal.backend.domain.diary.dto.req.KeywordEmotionDto;
import com.nanal.backend.domain.diary.entity.Keyword;
import com.nanal.backend.domain.diary.entity.KeywordEmotion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class KeywordDto {
    @Size(max = 5, message = "keyword 는 최대 5개의 문자만 입력 가능합니다.")
    private String keyword;

    @Valid
    private List<KeywordEmotionDto> keywordEmotions;

    public static List<KeywordEmotionDto> makeKeywordDtoList(Keyword keyword) {
        List<KeywordEmotionDto> keywordEmotionDtos = new ArrayList<>();
        for (KeywordEmotion k : keyword.getKeywordEmotions()) {
            KeywordEmotionDto keywordEmotionDto = new KeywordEmotionDto(k.getEmotion());

            keywordEmotionDtos.add(keywordEmotionDto);
        }

        return keywordEmotionDtos;
    }
}