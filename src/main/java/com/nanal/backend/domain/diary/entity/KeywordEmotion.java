package com.nanal.backend.domain.diary.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Table(name = "keyword_emotion")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class KeywordEmotion {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keyword_emotion_id")
    private Long keywordEmotionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_id")
    private Keyword keyword;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emotion_id")
    private Emotion emotion;

    //==생성 메서드==//
    public static KeywordEmotion createKeywordEmotion(Emotion emotion) {
        return KeywordEmotion.builder()
                .emotion(emotion)
                .build();
    }

    public void setKeyword(Keyword keyword) {
        this.keyword = keyword;
    }
}
