package com.nanal.backend.domain.search.dto;

import com.nanal.backend.domain.diary.dto.req.KeywordDto;
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
public class RespSearchDto {
    private List<DiaryDto> diaryList;

    private List<RetrospectDto> retrospectList;

    static public class DiaryDto{
        private String content;

        private LocalDateTime writeDate;

        private Boolean editStatus;

        private List<KeywordDto> keywords = new ArrayList<>();
    }

    static public class RetrospectDto{

    }
}
