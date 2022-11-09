package com.nanal.backend.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Table(name = "keyword")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Keyword {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keyword_id")
    private Long keywordId;

    // word -> keyword 로 테스트
    @Column(length = 30, nullable = false)
    private String word;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    private Diary diary;

    @OneToMany(mappedBy = "keyword", cascade = CascadeType.ALL)
    private List<KeywordEmotion> keywordEmotions = new ArrayList<>();

    //==수정 메서드==//
    public void changeDiary(Diary diary) {
        this.diary = diary;
    }

    public void changeWord(String word) {
        this.word = word;
    }

    //==연관관계 메서드==//
    public void addKeywordEmotion(KeywordEmotion keywordEmotion) {
        keywordEmotions.add(keywordEmotion);
        keywordEmotion.changeKeyword(this);
    }

    //==생성 메서드==//
    public static Keyword createKeyword(String word, List<KeywordEmotion> keywordEmotions) {
        Keyword keyword = new Keyword();

        for (KeywordEmotion keywordEmotion : keywordEmotions) {
            keyword.addKeywordEmotion(keywordEmotion);
        }

        keyword.changeWord(word);

        return keyword;
    }
}
