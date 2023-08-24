package com.nanal.backend.domain.sponsor.entity;

import com.nanal.backend.domain.sponsor.enumerate.Goods;
import lombok.*;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "sponsor")
@Entity
public class Sponsor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sponsor_id")
    private Long sponsorId;

    @Column(nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Goods goods;

    private Boolean isConfirmed;

    //==설정 메서드==//
    public void changeisConfirmed(Boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }
}
