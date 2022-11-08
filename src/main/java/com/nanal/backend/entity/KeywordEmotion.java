package com.nanal.backend.entity;


import lombok.Getter;

import javax.persistence.*;

@Getter
@Table(name = "keyword_emotion")
@Entity
public class KeywordEmotion {

    @Id
    @GeneratedValue
    private Long keywordEmotionId;

    @ManyToOne
    @JoinColumn(name = "keyword_id")
    private Keyword keyword;

    @ManyToOne
    @JoinColumn(name = "emotion_id")
    private Emotion emotion;
}
