package com.nanal.backend.domain.diary.entity;


import com.nanal.backend.domain.diary.dto.req.KeywordDto;
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
    private EmotionList emotionList;

    //==생성 메서드==//
    public static Keyword createKeyword(Diary diary, KeywordDto keywordDto) {
        return Keyword.builder()
                .word(keywordDto.getKeyword())
                .diary(diary)
                .emotionList(EmotionList.createEmotionList(keywordDto.getKeywordEmotions()))
                .build();
    }

    //==연관관계 메서드==//
    public void setDiary(Diary diary) {
        this.diary = diary;
    }

    //==비즈니스 메서드==//

}
