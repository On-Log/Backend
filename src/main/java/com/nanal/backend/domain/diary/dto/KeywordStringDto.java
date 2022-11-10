package com.nanal.backend.domain.diary.dto;

import com.nanal.backend.entity.Keyword;
import com.nanal.backend.entity.KeywordEmotion;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class KeywordStringDto {

    private String keyword;

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
