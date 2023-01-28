package com.nanal.backend.domain.diary.entity;


import com.nanal.backend.global.config.BaseTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Table(name = "keyword")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Keyword extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keyword_id")
    private Long keywordId;

    @Column(nullable = false, length = 5)
    private String word;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    private Diary diary;

    @Embedded
    private EmotionList EmotionList;

    //@OneToMany(mappedBy = "keyword", cascade = CascadeType.ALL)
    //private List<KeywordEmotion> keywordEmotions = new ArrayList<>();

    //==수정 메서드==//
    public void changeDiary(Diary diary) {
        this.diary = diary;
    }

    public void changeWord(String keyword) {
        this.word = keyword;
    }

    //==연관관계 메서드==//


    //==생성 메서드==//
    /*public static Keyword makeKeyword(String word, List<KeywordEmotion> keywordEmotions) {
        Keyword keyword = new Keyword();

        for (KeywordEmotion keywordEmotion : keywordEmotions) {
            keyword.addKeywordEmotion(keywordEmotion);
        }

        keyword.changeWord(word);

        return keyword;
    }*/
}
