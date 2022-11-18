package com.nanal.backend.domain.retrospect.dto;

import com.nanal.backend.entity.Diary;
import com.nanal.backend.entity.Keyword;
import com.nanal.backend.entity.KeywordEmotion;
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

    private List<String> KeywordAndEmotion;

    public static RespGetKeywordAndEmotionDto makeRespGetKeywordAndEmotionDto(List<Diary> diaries) {
        //일기 정보를 담을 String 리스트
        List<String> KeywordAndEmotion = new ArrayList<>();
        String str = null;
        for(Diary d : diaries) {
            for(Keyword k : d.getKeywords()){
                str = d.getWriteDate() + " " + k.getWord() + " ";
                for(KeywordEmotion e : k.getKeywordEmotions()){
                    str += e.getEmotion() + " ";
                }
            }
            KeywordAndEmotion.add(str);
        }

        RespGetKeywordAndEmotionDto respGetKeywordAndEmotionDto = RespGetKeywordAndEmotionDto.builder()
                .KeywordAndEmotion(KeywordAndEmotion)
                .build();

        return respGetKeywordAndEmotionDto;
    }

}
