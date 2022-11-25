package com.nanal.backend.domain.retrospect.dto.resp;

import com.nanal.backend.domain.diary.entity.Keyword;
import com.nanal.backend.domain.diary.entity.KeywordEmotion;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
public class KeywordStringsDto {
    @Size(max = 5, message = "최대 5개의 문자만 입력 가능합니다.")
    private String keyword;

    private List<String> keywordEmotions;

    public static KeywordStringsDto makeKeywordStringsDto(Keyword keyword) {
        KeywordStringsDto keywordStringsDto = new KeywordStringsDto();
        keywordStringsDto.setKeyword(keyword.getWord());
        List<String> keywordEmotions = new ArrayList<>();
        for (KeywordEmotion ke : keyword.getKeywordEmotions()) {
            keywordEmotions.add(ke.getEmotion());

        }
        keywordStringsDto.setKeywordEmotions(keywordEmotions);
        return keywordStringsDto;
    }
}
