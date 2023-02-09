package com.nanal.backend.domain.diary.entity;


import com.nanal.backend.domain.diary.dto.req.KeywordDto;
import com.nanal.backend.domain.diary.dto.req.KeywordEmotionDto;
import com.nanal.backend.global.config.BaseTime;
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
public class Keyword extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keyword_id")
    private Long keywordId;

    @Column(nullable = false, length = 5)
    private String word;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    private Diary diary;

    @OneToMany(mappedBy = "keyword", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<KeywordEmotion> keywordEmotions = new ArrayList<>();

    //==생성 메서드==//
    public static Keyword createKeyword(Diary diary, KeywordDto keywordDto, List<Emotion> emotions) {

        Keyword keyword = Keyword.builder()
                .word(keywordDto.getKeyword())
                .diary(diary)
                .build();

        for (KeywordEmotionDto ke : keywordDto.getKeywordEmotions()) {
            for (Emotion e : emotions) {
                if (e.getEmotion().equals(ke.getEmotion())) {
                    KeywordEmotion keywordEmotion = KeywordEmotion.createKeywordEmotion(e);
                    keyword.addKeywordEmotion(keywordEmotion);
                    break;
                }
            }
        }

        return keyword;
    }

    //==연관관계 메서드==//
    public void setDiary(Diary diary) {
        this.diary = diary;
    }
    private void addKeywordEmotion(KeywordEmotion keywordEmotion) {
        keywordEmotions.add(keywordEmotion);
        keywordEmotion.setKeyword(this);
    }

    //==비즈니스 메서드==//

}
