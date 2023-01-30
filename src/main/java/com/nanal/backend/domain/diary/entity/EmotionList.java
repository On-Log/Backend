package com.nanal.backend.domain.diary.entity;

import com.nanal.backend.domain.diary.dto.req.KeywordEmotionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class EmotionList {

    private String firstEmotion;
    private String secondEmotion;
    private String thirdEmotion;

    public static EmotionList createEmotionList(List<KeywordEmotionDto> keywordEmotions) {
        return EmotionList.builder()
                .firstEmotion(keywordEmotions.get(0).getEmotion())
                .secondEmotion(keywordEmotions.get(1).getEmotion())
                .thirdEmotion(keywordEmotions.get(2).getEmotion())
                .build();
    }
}
