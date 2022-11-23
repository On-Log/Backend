package com.nanal.backend.domain.retrospect.dto;

import com.nanal.backend.entity.Diary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RespGetKeywordAndEmotionDto {
    private List<KeywordStringsDto> keywords;

    public static RespGetKeywordAndEmotionDto makeRespGetKeywordAndEmotionDto(List<Diary> diaries){
        List<KeywordStringsDto> keywords = new ArrayList<>();
        for(Diary d : diaries) {
            KeywordStringsDto keywordStringsDto = KeywordStringsDto.makeKeywordStringsDto(d);

            keywords.add(keywordStringsDto);
        }
        RespGetKeywordAndEmotionDto respGetKeywordAndEmotionDto = RespGetKeywordAndEmotionDto.builder()
                .keywords(keywords)
                .build();

        return respGetKeywordAndEmotionDto;
    }

}
