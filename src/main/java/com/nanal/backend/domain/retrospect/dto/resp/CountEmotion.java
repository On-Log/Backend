package com.nanal.backend.domain.retrospect.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CountEmotion {
    String emotion;
    Integer frequency;

    public static CountEmotion makeCountEmotion(String emotion, int frequency) {

        CountEmotion countEmotion = CountEmotion.builder()
                .emotion(emotion)
                .frequency(frequency)
                .build();

        return countEmotion;
    }
}
