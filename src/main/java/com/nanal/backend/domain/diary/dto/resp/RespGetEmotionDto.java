package com.nanal.backend.domain.diary.dto.resp;

import com.nanal.backend.domain.diary.entity.Emotion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RespGetEmotionDto {

    private List<String> emotion;

    public static RespGetEmotionDto createRespGetEmotionDto(List<Emotion> emotions) {
        List<String> emotionWords = emotions.stream()
                .map(Emotion::getEmotion)
                .collect(Collectors.toList());

        return new RespGetEmotionDto(emotionWords);
    }
}
