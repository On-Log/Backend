package com.nanal.backend.domain.retrospect.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nanal.backend.entity.Diary;
import com.nanal.backend.entity.Keyword;
import com.nanal.backend.entity.KeywordEmotion;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class KeywordWriteDateDto {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime writeDate;

    private List<KeywordStringsDto> keywords;

    public static KeywordWriteDateDto makeKeywordWriteDateDto(Diary d) {
        List<KeywordStringsDto> keywords = new ArrayList<>();
        for(Keyword k : d.getKeywords()){
            System.out.println(k.getWord());
            KeywordStringsDto keywordStringsDto = KeywordStringsDto.makeKeywordStringsDto(k);

            keywords.add(keywordStringsDto);
        }
        KeywordWriteDateDto  keywordWriteDateDto = KeywordWriteDateDto.builder()
                .writeDate(d.getWriteDate())
                .keywords(keywords)
                .build();

        return keywordWriteDateDto;
    }
}
