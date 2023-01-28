package com.nanal.backend.domain.diary.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class EmotionList {

    private String firstEmotion;
    private String secondEmotion;
    private String thirdEmotion;
}
