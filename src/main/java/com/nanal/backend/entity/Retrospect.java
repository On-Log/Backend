package com.nanal.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "retrospect")
@Entity
@DynamicUpdate
public class Retrospect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "retrospect_id")
    private Long retrospectId;

    private LocalDateTime writeDate;

    private Boolean editStatus;

    @Column(length = 30, nullable = false)
    private String goal;
    @OneToMany(mappedBy = "retrospect", cascade = CascadeType.ALL)
    private List<RetrospectKeyword> retrospectKeywords = new ArrayList<>();

    @OneToMany(mappedBy = "retrospect", cascade = CascadeType.ALL)
    private List<RetrospectContent> retrospectContents = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //==수정 메서드==//
    public void changeEditStatus(Boolean editStatus) { this.editStatus = editStatus; }

    //==설정 메서드==//
    public void changeGoalAndWriteDateAndEditStatus(String goal, LocalDateTime writeDate, Boolean editStatus) {
        this.goal = goal;
        this.writeDate = writeDate;
        this.editStatus = editStatus;
    }
    //==연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.getRetrospects().add(this);
    }

    public void addKeyword(RetrospectKeyword retrospectKeyword) {
        retrospectKeywords.add(retrospectKeyword);
        retrospectKeyword.changeRetrospect(this);
    }

    public void addContent(RetrospectContent retrospectContent) {
        retrospectContents.add(retrospectContent);
        retrospectContent.changeRetrospect(this);
    }

    //==생성 메서드==//
    public static Retrospect makeRetrospect(Member member, List<RetrospectKeyword> retrospectKeywords, List<RetrospectContent> retrospectContents, String goal, LocalDateTime writeDate) {

        Retrospect retrospect = new Retrospect();

        // 연관 관계 생성
        retrospect.setMember(member);
        for (RetrospectKeyword retrospectKeyword : retrospectKeywords) {
            retrospect.addKeyword(retrospectKeyword);
        }
        for (RetrospectContent retrospectContent : retrospectContents) {
            retrospect.addContent(retrospectContent);
        }

        // 속성 설정
        retrospect.changeGoalAndWriteDateAndEditStatus(goal, writeDate, true);

        return retrospect;
    }

}
