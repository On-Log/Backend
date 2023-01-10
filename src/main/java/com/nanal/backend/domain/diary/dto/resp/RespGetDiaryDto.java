package com.nanal.backend.domain.diary.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nanal.backend.domain.diary.dto.req.KeywordDto;
import com.nanal.backend.domain.diary.entity.Diary;
import com.nanal.backend.domain.diary.entity.Keyword;
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

    //@JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime writeDate;

    private String content;

    private List<KeywordDto> keywords;

    public static RespGetDiaryDto makeRespGetDiaryDto(Diary selectDiary) {
        List<KeywordDto> keywordDtos = new ArrayList<>();

        for (Keyword k : selectDiary.getKeywords()) {
            KeywordDto keywordDto = new KeywordDto(k.getWord(), KeywordDto.makeKeywordDtoList(k));
            keywordDtos.add(keywordDto);
        }

        RespGetDiaryDto respGetDiaryDto = RespGetDiaryDto.builder()
                .writeDate(selectDiary.getWriteDate())
                .content(selectDiary.getContent())
                .keywords(keywordDtos)
                .build();
        return respGetDiaryDto;
    }
}
