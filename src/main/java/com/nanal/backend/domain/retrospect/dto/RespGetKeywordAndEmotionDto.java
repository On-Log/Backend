package com.nanal.backend.domain.retrospect.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nanal.backend.domain.diary.dto.KeywordStringDto;
import com.nanal.backend.entity.Diary;
import com.nanal.backend.entity.Keyword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RespGetKeywordAndEmotionDto {

    private List<KeywordWriteDateDto> keywords;

    public static RespGetKeywordAndEmotionDto makeRespGetKeywordAndEmotionDto(List<Diary> diaries){
        List<KeywordWriteDateDto> keywordList = new ArrayList<>();
        for(Diary d : diaries) {
            KeywordWriteDateDto keywordWriteDateDto = KeywordWriteDateDto.makeKeywordWriteDateDto(d);

            keywordList.add(keywordWriteDateDto);
        }
        RespGetKeywordAndEmotionDto respGetKeywordAndEmotionDto = RespGetKeywordAndEmotionDto.builder()
                .keywords(keywordList)
                .build();

        return respGetKeywordAndEmotionDto;
    }

}
