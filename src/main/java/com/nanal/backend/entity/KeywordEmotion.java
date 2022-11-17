package com.nanal.backend.entity;


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

    @Column(nullable = false, length = 5)
    private String emotion;

    //==설정 메서드==//
    private void changeEmotion(String emotion) {
        this.emotion = emotion;
    }

    public void changeKeyword(Keyword keyword) {
        this.keyword = keyword;
    }

    //==연관관계 메서드==//


    //==생성 메서드==//
    public static KeywordEmotion makeKeywordEmotion(String emotion) {
        KeywordEmotion keywordEmotion = new KeywordEmotion();
        keywordEmotion.changeEmotion(emotion);
        return keywordEmotion;
    }


}
