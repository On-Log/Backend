package com.nanal.backend.domain.auth.entity;

import com.nanal.backend.domain.auth.enumerate.MemberProvider;
import com.nanal.backend.domain.diary.entity.Diary;
import com.nanal.backend.domain.mypage.exception.ChangeRetrospectDateException;
import com.nanal.backend.domain.mypage.exception.RetrospectDayDupException;
import com.nanal.backend.domain.retrospect.entity.Retrospect;
import com.nanal.backend.global.config.BaseTime;
import lombok.*;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
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

    @Column(nullable = false, unique = true)
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

    //==생성 메서드==//
    public static Member valueOf(OAuth2User oAuth2User) {
        var attributes = oAuth2User.getAttributes();
        return Member.builder()
                .socialId((String) attributes.get("socialId"))
                .provider((MemberProvider) attributes.get("provider"))
                .name((String) attributes.get("name"))
                .nickname((String) attributes.get("nickname"))
                .email((String) attributes.get("email"))
                // 당일로 회고일 설정
                .retrospectDay(LocalDate.now().getDayOfWeek())
                .prevRetrospectDate(LocalDateTime.now().minusDays(30))
                .gender((String) attributes.get("gender"))
                .ageRange((String) attributes.get("age"))
                .role(Member.Role.USER)
                .build();
    }

    //==수정 메서드==//
    public void updateNickname(String nickname) { this.nickname = nickname; }

    public void updateRetrospectDay(DayOfWeek retrospectDay) {
        // 요청 회고요일로 변경 가능한지 검증
        LocalDateTime now = LocalDateTime.now();
        if(!verifyChangingRetrospectDate(now)) throw ChangeRetrospectDateException.EXCEPTION;
        if(isSameRetrospectDay(retrospectDay)) throw RetrospectDayDupException.EXCEPTION;

        this.retrospectDay = retrospectDay;
        this.prevRetrospectDate = now;
    }

    //==비즈니스 메서드==//
    private Boolean isSameRetrospectDay(DayOfWeek retrospectDay) {
        return this.retrospectDay.equals(retrospectDay);
    }

    public Boolean verifyChangingRetrospectDate(LocalDateTime now) {
        return ChronoUnit.DAYS.between(prevRetrospectDate, now) >= 30 ? true : false;
    }

    public Integer getServiceLife() {
        return Period.between(this.createdAt.toLocalDate(), LocalDateTime.now().toLocalDate()).getDays() + 1;
    }

    public Boolean isRetrospectDay(LocalDateTime now) {
        return now.getDayOfWeek().equals(retrospectDay);
    }
}


