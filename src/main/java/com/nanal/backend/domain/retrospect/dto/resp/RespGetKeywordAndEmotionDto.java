package com.nanal.backend.domain.retrospect.dto.resp;

import com.nanal.backend.domain.diary.entity.Diary;
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
    private Boolean isInTime;

    private LocalDateTime currentTime;

    private List<KeywordWriteDateDto> weeklyKeywords;

    public static RespGetKeywordAndEmotionDto makeRespGetKeywordAndEmotionDto(boolean isInTime, LocalDateTime currentTime, List<Diary> diaries){
        List<KeywordWriteDateDto> keywordList = new ArrayList<>();
        for(Diary d : diaries) {
            KeywordWriteDateDto keywordWriteDateDto = KeywordWriteDateDto.makeKeywordWriteDateDto(d);

            keywordList.add(keywordWriteDateDto);
        }
        RespGetKeywordAndEmotionDto respGetKeywordAndEmotionDto = RespGetKeywordAndEmotionDto.builder()
                .isInTime(isInTime)
                .currentTime(currentTime)
                .weeklyKeywords(keywordList)
                .build();

        return respGetKeywordAndEmotionDto;
    }


}
