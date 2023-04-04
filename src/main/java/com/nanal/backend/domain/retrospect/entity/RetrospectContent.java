package com.nanal.backend.domain.retrospect.entity;

import com.nanal.backend.domain.retrospect.dto.resp.RetrospectContentDto;
import com.nanal.backend.global.config.BaseTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Getter
@Table(name = "retrospect_content")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class RetrospectContent extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "retrospect_content_id")
    private Long retrospectContentId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "retrospect_id")
    private Retrospect retrospect;

    @Column(length = 200, nullable = false)
    private String question;

    @Column(length = 300)
    private String answer;

    //==수정 메서드==//

    public void changeAnswer(String answer){ this.answer = answer; }

    //==연관관계 메서드==//

    //==생성 메서드==//
    public static RetrospectContent makeRetrospectContent(Retrospect retrospect, RetrospectContentDto retrospectContentDto) {
        RetrospectContent retrospectContent = RetrospectContent.builder()
                .retrospect(retrospect)
                .question(retrospectContentDto.getQuestion())
                .answer(retrospectContentDto.getAnswer())
                .build();
        return retrospectContent;
    }


}
