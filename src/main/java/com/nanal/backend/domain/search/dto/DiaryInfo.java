package com.nanal.backend.domain.search.dto;

import com.nanal.backend.domain.diary.dto.req.KeywordDto;
import com.nanal.backend.domain.diary.entity.Diary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DiaryInfo {

    private Boolean existMore = false;
    private Integer nextDiaryCount;
    private List<DiaryDto> diaryDtoList;

    public DiaryInfo(List<Diary> diaryList, Integer nextDiaryCount) {
        if(nextDiaryCount > 0) this.existMore = true;
        this.nextDiaryCount = nextDiaryCount;

        this.diaryDtoList = diaryList.stream()
                .map(DiaryDto::new)
                .collect(Collectors.toList());
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    static public class DiaryDto{
        private Long diaryId;
        private LocalDateTime writeDate;
        private String content;
        private Boolean editStatus;
        private List<KeywordDto> keywords;

        public DiaryDto(Diary diary) {
            this.diaryId = diary.getDiaryId();
            this.writeDate = diary.getWriteDate();

            String content = diary.getContent();
            int index = content.indexOf(content);
            if (index != -1) {
                content = "..." + content.substring(index);
            }

            this.content = content;
            this.editStatus = diary.getEditStatus();
            this.keywords = diary.getKeywords().stream()
                    .map(KeywordDto::new)
                    .collect(Collectors.toList());
        }
    }
}
