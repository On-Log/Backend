package com.nanal.backend.domain.search.dto;

import com.nanal.backend.domain.diary.dto.req.KeywordDto;
import com.nanal.backend.domain.diary.entity.Diary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Arrays;
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

    public DiaryInfo(List<Diary> diaryList, Integer nextDiaryCount, String searchWord) {
        if(nextDiaryCount > 0) this.existMore = true;
        this.nextDiaryCount = nextDiaryCount;

        this.diaryDtoList = diaryList.stream()
                .map(diary -> new DiaryDto(diary, searchWord))
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
        private List<String> keywords;

        public DiaryDto(Diary diary, String searchWord) {
            this.diaryId = diary.getDiaryId();
            this.writeDate = diary.getWriteDate();
            this.content = parseContent(diary.getContent(), searchWord);
            this.keywords = diary.getKeywords().stream()
                    .map(keyword -> keyword.getWord())
                    .collect(Collectors.toList());
        }

        public static String parseContent(String content, String searchWord) {
            String[] words = content.split("\\s+");
            int targetIndex = -1;

            for (int i = 0; i < words.length; i++) {
                if (words[i].contains(searchWord)) {
                    targetIndex = i;
                    break;
                }
            }

            if (targetIndex >= 3) {
                return "... " + String.join(" ", Arrays.asList(words).subList(targetIndex - 3, targetIndex + 1));
            } else {
                return content;
            }
        }
    }
}
