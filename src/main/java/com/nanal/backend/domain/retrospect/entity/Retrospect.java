package com.nanal.backend.domain.retrospect.entity;

import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.domain.retrospect.dto.req.ReqEditRetroDto;
import com.nanal.backend.domain.retrospect.dto.req.ReqSaveRetroDto;
import com.nanal.backend.global.config.BaseTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "retrospect")
@Entity
@DynamicUpdate
public class Retrospect extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "retrospect_id")
    private Long retrospectId;

    private LocalDateTime writeDate;

    private Boolean editStatus;

    @Column(length = 30, nullable = false)
    private String goal;
    @OneToMany(mappedBy = "retrospect", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<RetrospectKeyword> retrospectKeywords = new ArrayList<>();

    @OneToMany(mappedBy = "retrospect", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<RetrospectContent> retrospectContents = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //==수정 메서드==//
    public void changeAnswer(ReqEditRetroDto reqEditRetroDto) {
        retrospectContents.get(reqEditRetroDto.getIndex()).changeAnswer(reqEditRetroDto.getAnswer());
    }
    //==설정 메서드==//
    //==연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.getRetrospects().add(this);
    }

    //==생성 메서드==//
    public static Retrospect createRetrospect(Member member, ReqSaveRetroDto reqSaveRetroDto) {
        Retrospect retrospect = Retrospect.builder()
                .writeDate(reqSaveRetroDto.getCurrentDate())
                .editStatus(true)
                .goal(reqSaveRetroDto.getGoal())
                .build();


        List<RetrospectKeyword> retrospectKeywordList = reqSaveRetroDto.getKeywords().stream()
                .map(retrospectKeywordDto -> RetrospectKeyword.makeRetrospectKeyword(retrospect, retrospectKeywordDto))
                .collect(Collectors.toList());
        retrospect.getRetrospectKeywords().addAll(retrospectKeywordList);

        List<RetrospectContent> retrospectContentList = reqSaveRetroDto.getContents().stream()
                .map(retrospectContentDto -> RetrospectContent.makeRetrospectContent(retrospect, retrospectContentDto))
                .collect(Collectors.toList());
        retrospect.getRetrospectContents().addAll(retrospectContentList);

        retrospect.setMember(member);

        return retrospect;
    }
}
