package com.nanal.backend.domain.retrospect.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nanal.backend.domain.diary.entity.Diary;
import com.nanal.backend.domain.diary.entity.Keyword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class KeywordWriteDateDto {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime writeDate;

    private List<KeywordDto> keywords;

    public static KeywordWriteDateDto makeKeywordWriteDateDto(Diary d) {
        int index = 0;
        List<KeywordDto> keywords = new ArrayList<>();
        for(Keyword k : d.getKeywords()){
            String key = makeKey(d.getWriteDate(), k, index);
            KeywordDto keywordDto = new KeywordDto(key, k.getWord(), KeywordDto.makeKeywordDtoList(k));

            keywords.add(keywordDto);
            index++;
        }
        KeywordWriteDateDto  keywordWriteDateDto = KeywordWriteDateDto.builder()
                .writeDate(d.getWriteDate())
                .keywords(keywords)
                .build();

        return keywordWriteDateDto;
    }

    public static String makeKey(LocalDateTime date, Keyword k, Integer index) {
        String key = date.toString();
        key = key.substring(0,10);
        key = key.replaceAll("-", "");
        key = key + "_" + index.toString();
        return key;
    }
}
