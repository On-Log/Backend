package com.nanal.backend.domain.retrospect.entity;


import com.nanal.backend.domain.retrospect.dto.resp.RetrospectKeywordDto;
import com.nanal.backend.global.config.BaseTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Table(name = "retrospect_keyword")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class RetrospectKeyword extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "retrospect_keyword_id")
    private Long retrospectKeywordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "retrospect_id")
    private Retrospect retrospect;

    @Column(length = 5, nullable = false)
    private String keyword;

    @Column(length = 20, nullable = false)
    private String classify;

    //==수정 메서드==//
    public void changeRetrospect(Retrospect retrospect) {
        this.retrospect = retrospect;
    }

    public void changeKeyWord(String keyword) {
        this.keyword = keyword;
    }

    public void changeClassify(String classify){ this.classify = classify; }

    //==연관관계 메서드==//

    //==생성 메서드==//
    public static RetrospectKeyword makeRetrospectKeyword(Retrospect retrospect, RetrospectKeywordDto retrospectKeywordDto) {
        RetrospectKeyword retrospectKeyword = RetrospectKeyword.builder()
                .retrospect(retrospect)
                .keyword(retrospectKeywordDto.getKeyword())
                .classify(retrospectKeywordDto.getClassify())
                .build();

        return retrospectKeyword;
    }
}
