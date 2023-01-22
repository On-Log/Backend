package com.nanal.backend.domain.auth.entity;

import com.nanal.backend.domain.auth.enumerate.MemberProvider;
import com.nanal.backend.domain.diary.entity.Diary;
import com.nanal.backend.domain.mypage.exception.ResetAvailException;
import com.nanal.backend.domain.mypage.exception.RetrospectDayDupException;
import com.nanal.backend.domain.retrospect.entity.Retrospect;
import com.nanal.backend.global.config.BaseTime;
import com.nanal.backend.global.response.ErrorCode;
import lombok.*;

import javax.persistence.*;
import java.time.DayOfWeek;
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
    private Boolean resetAvail;

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

    public void setResetAvail(Boolean resetAvail) {
        this.resetAvail = resetAvail;
    }

    public void updateRetrospectDay(DayOfWeek retrospectDay) {
        // 요청 회고요일로 변경 가능한지 검증
        checkUpdateRetrospectDay(retrospectDay);

        this.retrospectDay = retrospectDay;
    }

    public void checkUpdateRetrospectDay(DayOfWeek retrospectDay) {
        // 회고일이 같은 경우, error.
        if (checkRetrospectDay(retrospectDay)) {throw new RetrospectDayDupException(ErrorCode.RETROSPECT_DAY_DUPLICATION.getMessage());}
        // resetAvail이 false일 때(이번달에 이미 회고요일 변경이 있었을 때), error.
        if (checkResetAvail()) {throw new ResetAvailException(ErrorCode.RESET_AVAIL_FALSE.getMessage());}
    }

    public boolean checkRetrospectDay(DayOfWeek retrospectDay) {
        return getRetrospectDay().equals(retrospectDay);
    }

    public boolean checkResetAvail() {
        if(getResetAvail() == false) return true;

        setResetAvail(false);
        return false;
    }
}


