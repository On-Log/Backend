package com.nanal.backend.domain.search.dto;

import com.nanal.backend.domain.diary.dto.req.KeywordDto;
import com.nanal.backend.domain.retrospect.dto.RetrospectContentDto;
import com.nanal.backend.domain.retrospect.dto.RetrospectKeywordDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RespSearchDto {
    private List<DiaryDto> diaryList;

    private List<RetrospectDto> retrospectList;

    static public class DiaryDto{
        private LocalDateTime writeDate;

        private String content;

        private Boolean editStatus;

        private List<KeywordDto> keywords;
    }

    static public class RetrospectDto{
        private LocalDateTime writeDate;

        private List<RetrospectContentDto> contents;

        private List<RetrospectKeywordDto> keywords;
    }
}
