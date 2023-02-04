package com.nanal.backend.domain.retrospect.dto.resp;

import com.nanal.backend.domain.diary.entity.Diary;
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

    private List<KeywordWriteDateDto> weeklyKeywords;

    public static RespGetKeywordAndEmotionDto makeRespGetKeywordAndEmotionDto(List<Diary> diaries){
        List<KeywordWriteDateDto> keywordList = new ArrayList<>();
        for(Diary d : diaries) {
            KeywordWriteDateDto keywordWriteDateDto = KeywordWriteDateDto.makeKeywordWriteDateDto(d);

            keywordList.add(keywordWriteDateDto);
        }
        RespGetKeywordAndEmotionDto respGetKeywordAndEmotionDto = RespGetKeywordAndEmotionDto.builder()
                .weeklyKeywords(keywordList)
                .build();

        return respGetKeywordAndEmotionDto;
    }

}
