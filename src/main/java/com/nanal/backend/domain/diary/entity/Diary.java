package com.nanal.backend.domain.diary.entity;

import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.domain.diary.dto.req.ReqDiaryDto;
import com.nanal.backend.domain.diary.dto.req.ReqSaveDiaryDto;
import com.nanal.backend.domain.diary.exception.DiaryChangeUnavailable;
import com.nanal.backend.global.config.BaseTime;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private final List<Keyword> keywords = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //==연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.getDiaries().add(this);
    }

    //==생성 메서드==//
    public static Diary createDiary(Member member, ReqSaveDiaryDto reqSaveDiaryDto, List<Emotion> findEmotions) {
        Diary diary = Diary.builder()
                .content(reqSaveDiaryDto.getContent())
                .writeDate(reqSaveDiaryDto.getDate().withNano(0))
                .editStatus(true)
                .build();

        // 키워드 생성 후 삽입
        List<Keyword> keywordList = reqSaveDiaryDto.getKeywords().stream()
                .map(keywordDto -> Keyword.createKeyword(diary, keywordDto, findEmotions))
                .collect(Collectors.toList());
        diary.getKeywords().addAll(keywordList);

        // 연관 관계 생성
        diary.setMember(member);

        return diary;
    }

    //==설정 메서드==//
    public void changeEditStatus(Boolean editStatus) {
        this.editStatus = editStatus;
    }

    public void updateDiary(ReqDiaryDto reqDiaryDto, List<Emotion> findEmotions) {
        this.content = reqDiaryDto.getContent();

        this.keywords.clear();
        List<Keyword> keywordList = reqDiaryDto.getKeywords().stream()
                .map(keywordDto -> Keyword.createKeyword(this, keywordDto, findEmotions))
                .collect(Collectors.toList());
        this.keywords.addAll(keywordList);
    }

    //==비즈니스 메서드==//
    public void checkUpdatable() {
        if(!editStatus) throw DiaryChangeUnavailable.EXCEPTION;
    }
}
