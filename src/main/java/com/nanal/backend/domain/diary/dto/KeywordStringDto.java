package com.nanal.backend.domain.diary.dto;

import com.nanal.backend.entity.Keyword;
import com.nanal.backend.entity.KeywordEmotion;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
public class KeywordStringDto {
    @Size(max = 5, message = "최대 5개의 문자만 입력 가능합니다.")
    private String keyword;

    @NotNull
    private List<String> keywordEmotions;

    public static KeywordStringDto makeKeywordStringDto(Keyword keyword) {
        KeywordStringDto keywordStringDto = new KeywordStringDto();
        keywordStringDto.setKeyword(keyword.getWord());

        List<String> keywordEmotions = new ArrayList<>();
        for (KeywordEmotion ke : keyword.getKeywordEmotions()) {
            keywordEmotions.add(ke.getEmotion());

        }
        keywordStringDto.setKeywordEmotions(keywordEmotions);
        return keywordStringDto;
    }
}
