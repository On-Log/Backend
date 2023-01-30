package com.nanal.backend.domain.diary.entity;

import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.domain.diary.dto.req.ReqDiaryDto;
import com.nanal.backend.global.config.BaseTime;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "diary")
@Entity
public class Diary extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long diaryId;

    @Column(length = 500, nullable = false)
    private String content;

    private LocalDateTime writeDate;

    private Boolean editStatus;

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Keyword> keywords = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    //==연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.getDiaries().add(this);
    }

    public void addKeyword(Keyword keyword) {
        keywords.add(keyword);
        keyword.setDiary(this);
    }

    //==생성 메서드==//
    public static Diary makeDiary(Member member, List<Keyword> keywords, String content, LocalDateTime writeDate) {

        Diary diary = new Diary();

        // 연관 관계 생성
        diary.setMember(member);
        for (Keyword keyword : keywords) {
            diary.addKeyword(keyword);
        }

        // 속성 설정++
        diary.changeContentAndWriteDateAndEditStatus(content, writeDate, true);

        return diary;
    }

    //==설정 메서드==//
    public void changeContentAndWriteDateAndEditStatus(String content, LocalDateTime writeDate, Boolean editStatus) {
        this.content = content;
        this.writeDate = writeDate;
        this.editStatus = editStatus;
    }

    public void changeEditStatus(Boolean editStatus) {
        this.editStatus = editStatus;
    }

    public void update(ReqDiaryDto reqDiaryDto) {
        this.content = reqDiaryDto.getContent();

        this.keywords.clear();
        List<Keyword> keywordList = reqDiaryDto.getKeywords().stream()
                .map(keywordDto -> Keyword.updateKeyword(this, keywordDto))
                .collect(Collectors.toList());
        this.keywords.addAll(keywordList);
    }
}
