package com.nanal.backend.domain.retrospect.dto;

import com.nanal.backend.domain.diary.dto.KeywordStringDto;
import com.nanal.backend.entity.Diary;
import com.nanal.backend.entity.Keyword;
import com.nanal.backend.entity.KeywordEmotion;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class KeywordStringsDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;

    @Size(max = 5, message = "최대 5개의 문자만 입력 가능합니다.")
    private String keyword;

    private List<String> keywordEmotions;

    public static KeywordStringsDto makeKeywordStringsDto(Diary diary) {
        KeywordStringsDto keywordStringsDto = new KeywordStringsDto();
        keywordStringsDto.setDate(diary.getWriteDate());
        for (Keyword k : diary.getKeywords()) {
            keywordStringsDto.setKeyword(k.getWord());

            List<String> keywordEmotions = new ArrayList<>();
            for (KeywordEmotion ke : k.getKeywordEmotions()) {
                keywordEmotions.add(ke.getEmotion());

            }
            keywordStringsDto.setKeywordEmotions(keywordEmotions);
        }
        return keywordStringsDto;
    }
}
