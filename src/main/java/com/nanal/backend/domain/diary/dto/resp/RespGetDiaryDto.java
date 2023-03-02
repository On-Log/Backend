package com.nanal.backend.domain.diary.dto.resp;

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
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RespGetDiaryDto {

    private LocalDateTime writeDate;

    private String content;

    private List<KeywordDto> keywords;

    private Boolean editStatus;

    public static RespGetDiaryDto createRespGetDiaryDto(Diary selectDiary) {
        List<KeywordDto> keywordDtoList = selectDiary.getKeywords().stream()
                .map(keyword -> new KeywordDto(keyword))
                .collect(Collectors.toList());

        RespGetDiaryDto respGetDiaryDto = RespGetDiaryDto.builder()
                .writeDate(selectDiary.getWriteDate())
                .content(selectDiary.getContent())
                .keywords(keywordDtoList)
                .editStatus(selectDiary.getEditStatus())
                .build();
        return respGetDiaryDto;
    }
}
