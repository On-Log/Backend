package com.nanal.backend.entity;

import lombok.*;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter //setter 추가.
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
@Entity
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    // RFC 표준상 최대 320자.
    @Column(unique = true, nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 10)
    private String provider;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 20)
    private String nickname;

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

    // update created by yubin

    public void changeNickname(String nickname) { //member내에서는 닉네임의 update만 발생.
        System.out.println("-------member 내 update확인: "+nickname+" | this값 확인: "+this.nickname);
        this.nickname = nickname;
    }

    public void changeRetrospectDay(DayOfWeek retrospectDay) { //member내에서는 닉네임의 update만 발생.
        System.out.println("-------member 내 update확인: "+retrospectDay+" | this값 확인: "+this.retrospectDay);
        this.retrospectDay = retrospectDay;
    }
}


