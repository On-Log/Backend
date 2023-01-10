package com.nanal.backend.domain.retrospect.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nanal.backend.domain.diary.entity.Diary;
import com.nanal.backend.domain.diary.entity.Keyword;
import com.nanal.backend.domain.retrospect.dto.req.KeywordDto;
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

    private List<KeywordDto> keywords;

    public static KeywordWriteDateDto makeKeywordWriteDateDto(Diary d) {
        List<KeywordDto> keywords = new ArrayList<>();
        for(Keyword k : d.getKeywords()){
            KeywordDto keywordDto = new KeywordDto(k.getWord(), KeywordDto.makeKeywordDtoList(k));

            keywords.add(keywordDto);
        }
        KeywordWriteDateDto  keywordWriteDateDto = KeywordWriteDateDto.builder()
                .writeDate(d.getWriteDate())
                .keywords(keywords)
                .build();

        return keywordWriteDateDto;
    }
}
