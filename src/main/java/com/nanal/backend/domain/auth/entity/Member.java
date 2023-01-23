package com.nanal.backend.domain.auth.entity;

import com.nanal.backend.domain.auth.enumerate.MemberProvider;
import com.nanal.backend.domain.diary.entity.Diary;
import com.nanal.backend.domain.mypage.exception.ChangeRetrospectDateException;
import com.nanal.backend.domain.mypage.exception.RetrospectDayDupException;
import com.nanal.backend.domain.retrospect.entity.Retrospect;
import com.nanal.backend.global.config.BaseTime;
import com.nanal.backend.global.response.ErrorCode;
import lombok.*;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
@Entity
public class Member extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(nullable = false)
    private String socialId;

    // RFC 표준상 최대 320자.
    @Column(nullable = false, length = 50)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberProvider provider;

    @Column(nullable = false, length = 7)
    private String name;

    @Column(nullable = false, length = 7)
    private String nickname;

    @Column(nullable = false, length = 10)
    private String ageRange;

    @Column(nullable = false, length = 6)
    private String gender;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek retrospectDay;

    @Column(nullable = false)
    private LocalDateTime prevRetrospectDate;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Diary> diaries = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Retrospect> retrospects = new ArrayList<>();

    @Getter
    @RequiredArgsConstructor
    public enum Role {
        USER("ROLE_USER"), ADMIN("ROLE_ADMIN");

        private final String key;
    }

    //==수정 메서드==//
    public void updateNickname(String nickname) { this.nickname = nickname; }

    public void updateRetrospectDay(DayOfWeek retrospectDay) {
        // 요청 회고요일로 변경 가능한지 검증
        LocalDateTime now = LocalDateTime.now();
        verifyPrevRetrospectDate(now);
        if(isSameRetrospectDay(retrospectDay)) {throw new RetrospectDayDupException(ErrorCode.RETROSPECT_DAY_DUPLICATION.getMessage());}

        this.retrospectDay = retrospectDay;
        this.prevRetrospectDate = now;
    }

    private Boolean isSameRetrospectDay(DayOfWeek retrospectDay) {
        return this.retrospectDay.equals(retrospectDay);
    }

    public void verifyPrevRetrospectDate(LocalDateTime now) {
        if(!(ChronoUnit.DAYS.between(prevRetrospectDate, now) >= 29)) {
            throw new ChangeRetrospectDateException(
                ErrorCode.RETROSPECT_DATE_CHANGE_IMPOSSIBLE.getMessage(),
                prevRetrospectDate.plusDays(30));
        }
    }
}


