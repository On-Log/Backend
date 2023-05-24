package com.nanal.backend.domain.auth.entity;

import com.nanal.backend.domain.alarm.entity.Alarm;
import com.nanal.backend.domain.auth.dto.KakaoUserResponseDto;
import com.nanal.backend.domain.auth.dto.req.ReqRegisterDto;
import com.nanal.backend.domain.auth.enumerate.MemberProvider;
import com.nanal.backend.domain.diary.entity.Diary;
import com.nanal.backend.domain.mypage.exception.ChangeRetrospectDateException;
import com.nanal.backend.domain.mypage.exception.RetrospectDayDupException;
import com.nanal.backend.domain.retrospect.entity.Retrospect;
import com.nanal.backend.global.config.BaseTime;
import com.nanal.backend.global.security.AuthenticationUtil;
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
    @Convert(converter = AesConverter.class)
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberProvider provider;

    @Convert(converter = AesConverter.class)
    @Column(nullable = false)
    private String name;

    @Convert(converter = AesConverter.class)
    @Column(nullable = false)
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

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Alarm alarm;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Diary> diaries = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Retrospect> retrospects = new ArrayList<>();

    @Getter
    @RequiredArgsConstructor
    public enum Role {
        ONBOARDER("ROLE_ONBOARDER"), USER("ROLE_USER"), ADMIN("ROLE_ADMIN");

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
                .role((Role) attributes.get("role"))
                .build();
    }

    public static Member createNewMember(ReqRegisterDto reqRegisterDto) {
        return Member.builder()
                .socialId(MemberProvider.GENERAL + "@" + AuthenticationUtil.passwordEncoder.encode(reqRegisterDto.getEmail()))
                .provider(MemberProvider.GENERAL)
                .name(reqRegisterDto.getNickname())
                .nickname(reqRegisterDto.getNickname())
                .email(MemberProvider.GENERAL + "#" + reqRegisterDto.getEmail())
                .password(reqRegisterDto.getPassword())
                // 당일로 회고일 설정
                .retrospectDay(LocalDate.now().getDayOfWeek())
                .prevRetrospectDate(LocalDateTime.now().minusDays(30))
                .gender(reqRegisterDto.getGender())
                .ageRange(reqRegisterDto.getAgeRange())
                .role(Member.Role.USER)
                .build();
    }

    public static Member createKakaoMember(KakaoUserResponseDto kakaoUserResponseDto) {
        Member newMember = Member.builder()
                .socialId(MemberProvider.KAKAO + "@" + kakaoUserResponseDto.getId())
                .provider(MemberProvider.KAKAO)
                .name(kakaoUserResponseDto.getProperties().getNickname())
                .email(kakaoUserResponseDto.getKakaoAccount().getEmail())
                .password("undef")
                // 당일로 회고일 설정
                .retrospectDay(LocalDate.now().getDayOfWeek())
                .prevRetrospectDate(LocalDateTime.now().minusDays(30))
                .nickname(kakaoUserResponseDto.getProperties().getNickname())
                .gender(kakaoUserResponseDto.getKakaoAccount().getGender())
                .ageRange(kakaoUserResponseDto.getKakaoAccount().getAgeRange())
                .role(Member.Role.ONBOARDER)
                .build();

        newMember.setAlarm(Alarm.createAlarm(newMember));

        return newMember;
    }

    public static Member createAppleMember(String socialId, String email) {
        Member newMember = Member.builder()
                .socialId(MemberProvider.APPLE + "@" + socialId)
                .provider(MemberProvider.APPLE)
                .name("나나리")
                .email(email)
                .password("undef")
                // 당일로 회고일 설정
                .retrospectDay(LocalDate.now().getDayOfWeek())
                .prevRetrospectDate(LocalDateTime.now().minusDays(30))
                .nickname("나나리")
                .ageRange("undef")
                .gender("undef")
                .role(Role.ONBOARDER)
                .build();

        newMember.setAlarm(Alarm.createAlarm(newMember));

        return newMember;
    }

    //==수정 메서드==//
    public void setAlarm(Alarm alarm) { this.alarm = alarm; }

    public void updateNickname(String nickname) { this.nickname = nickname; }

    public void updateRetrospectDay(DayOfWeek retrospectDay) {
        // 요청 회고요일로 변경 가능한지 검증
        LocalDateTime now = LocalDateTime.now();
        if(!verifyChangingRetrospectDate(now)) throw ChangeRetrospectDateException.EXCEPTION;
        if(isSameRetrospectDay(retrospectDay)) throw RetrospectDayDupException.EXCEPTION;

        this.retrospectDay = retrospectDay;
        this.prevRetrospectDate = now;
    }

    public void setRetrospectDay(DayOfWeek retrospectDay) {
        this.retrospectDay = retrospectDay;
        this.role = Role.USER;
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


