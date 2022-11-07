package com.nanal.backend.domain.oauth.entity;

import lombok.*;

import javax.persistence.*;
import java.time.DayOfWeek;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member {

    @Id @GeneratedValue
    private Long memberId;

    @Column(unique = true, nullable = false, length = 40)
    private String email;

    @Column(nullable = false, length = 10)
    private String provider;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek retrospectDay;

    @Column(nullable = false)
    private Boolean resetAvail;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Getter
    @RequiredArgsConstructor
    public enum Role {
        USER("ROLE_USER"), ADMIN("ROLE_ADMIN");

        private final String key;
    }
}


