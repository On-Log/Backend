package com.nanal.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "diary")
@Entity
public class Diary {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long diaryId;

    @Column(length = 300, nullable = true)
    private String content;

    private LocalDateTime writeDate;

    private Boolean editStatus;

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL)
    private List<Keyword> keywords = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //==설정 메서드==//
    public void changeContentAndWriteDateAndEditStatus(String content, LocalDateTime writeDate, Boolean editStatus) {
        this.content = content;
        this.writeDate = writeDate;
        this.editStatus = editStatus;
    }

    //==연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.getDiaries().add(this);
    }

    public void addKeyword(Keyword keyword) {
        keywords.add(keyword);
        keyword.changeDiary(this);
    }

    //==생성 메서드==//
    public static Diary createDiary(Member member, List<Keyword> keywords, String content, LocalDateTime writeDate) {

        Diary diary = new Diary();

        // 연관 관계 생성
        diary.setMember(member);
        for (Keyword keyword : keywords) {
            diary.addKeyword(keyword);
        }

        // 속성 설정
        diary.changeContentAndWriteDateAndEditStatus(content, writeDate, true);

        return diary;
    }

}
