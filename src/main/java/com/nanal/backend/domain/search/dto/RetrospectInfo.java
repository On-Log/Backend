package com.nanal.backend.domain.search.dto;

import com.nanal.backend.domain.retrospect.entity.Retrospect;
import com.nanal.backend.domain.retrospect.entity.RetrospectContent;
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
public class RetrospectInfo {

    private Boolean existMore = false;
    private Integer nextRetrospectCount;
    private List<RetrospectDto> retrospectDtoList;

    public RetrospectInfo(List<Retrospect> retrospectList, Integer nextRetrospectCount, String searchWord) {
        if(nextRetrospectCount > 0) this.existMore = true;
        this.nextRetrospectCount = nextRetrospectCount;

        this.retrospectDtoList = retrospectList.stream()
                .map(retrospect -> new RetrospectDto(retrospect, searchWord))
                .collect(Collectors.toList());
    }


    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    static public class RetrospectDto{
        private Long retrospectId;
        private LocalDateTime writeDate;
        private String question;
        private String answer;

        public RetrospectDto(Retrospect retrospect, String searchWord) {
            this.retrospectId = retrospect.getRetrospectId();
            this.writeDate = retrospect.getWriteDate();

            // 검색 포함하는 질문들 중 가장 앞 질문
            RetrospectContent retrospectContent = retrospect.getRetrospectContents().stream()
                    .filter(content -> content.getAnswer().contains(searchWord))
                    .findFirst().get();
            Long questionOrder = retrospectContent.getRetrospectContentId();
            StringBuilder question = new StringBuilder();
            question.append(questionOrder).append(". ").append(retrospectContent.getQuestion());

            this.question = question.toString();
            this.answer = parseAnswer(retrospectContent.getAnswer(), searchWord);
        }

        public static String parseAnswer(String content, String searchWord) {
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
