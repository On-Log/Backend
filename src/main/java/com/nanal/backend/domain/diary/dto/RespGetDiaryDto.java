package com.nanal.backend.domain.diary.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nanal.backend.entity.Diary;
import com.nanal.backend.entity.Keyword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RespGetDiaryDto {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime writeDate;

    private String content;

    private List<KeywordStringDto> keywords;

    public static RespGetDiaryDto makeRespGetDiaryDto(Diary selectDiary) {
        List<KeywordStringDto> keywords = new ArrayList<>();
        for (Keyword k : selectDiary.getKeywords()) {
            KeywordStringDto keywordStringDto = KeywordStringDto.makeKeywordStringDto(k);

            keywords.add(keywordStringDto);
        }

        RespGetDiaryDto respGetDiaryDto = RespGetDiaryDto.builder()
                .writeDate(selectDiary.getWriteDate())
                .content(selectDiary.getContent())
                .keywords(keywords)
                .build();
        return respGetDiaryDto;
    }
}
