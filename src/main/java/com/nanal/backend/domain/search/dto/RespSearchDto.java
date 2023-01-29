package com.nanal.backend.domain.search.dto;

import com.nanal.backend.domain.diary.dto.req.KeywordDto;
import com.nanal.backend.domain.diary.entity.Diary;
import com.nanal.backend.domain.retrospect.dto.RetrospectContentDto;
import com.nanal.backend.domain.retrospect.dto.RetrospectKeywordDto;
import com.nanal.backend.domain.retrospect.entity.Retrospect;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class RespSearchDto {
    private List<DiaryDto> diaryDtoList;

    private List<RetrospectDto> retrospectDtoList;

    public RespSearchDto(List<Diary> diaryList, List<Retrospect> retrospectList) {
        this.diaryDtoList = diaryList.stream()
                .map(DiaryDto::new)
                .collect(Collectors.toList());
        this.retrospectDtoList = retrospectList.stream()
                .map(RetrospectDto::new)
                .collect(Collectors.toList());
    }

    @Data
    static public class DiaryDto{
        private LocalDateTime writeDate;

        private String content;

        private Boolean editStatus;

        private List<KeywordDto> keywords;

        public DiaryDto(Diary diary) {
            this.writeDate = diary.getWriteDate();
            this.content = diary.getContent();
            this.editStatus = diary.getEditStatus();
            this.keywords = diary.getKeywords().stream()
                    .map(KeywordDto::new)
                    .collect(Collectors.toList());
        }
    }

    @Data
    static public class RetrospectDto{
        private LocalDateTime writeDate;

        private List<RetrospectContentDto> contents;

        private List<RetrospectKeywordDto> keywords;

        public RetrospectDto(Retrospect retrospect) {
            this.writeDate = retrospect.getWriteDate();
            this.contents = retrospect.getRetrospectContents().stream()
                    .map(RetrospectContentDto::new)
                    .collect(Collectors.toList());
            this.keywords = retrospect.getRetrospectKeywords().stream()
                    .map(RetrospectKeywordDto::new)
                    .collect(Collectors.toList());
        }
    }
}
