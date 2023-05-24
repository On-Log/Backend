package com.nanal.backend.domain.alarm.entity;

import com.nanal.backend.domain.alarm.dto.ReqUpdateDiaryAlarm;
import com.nanal.backend.domain.alarm.dto.ReqUpdateRetrospectAlarm;
import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.global.config.BaseTime;
import lombok.*;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "alarm")
@Entity
public class Alarm extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long alarmId;

    @Column(nullable = false)
    private Boolean diaryActive;

    @Column(length = 5, nullable = false)
    private String diary;

    @Column(nullable = false)
    private Boolean retrospectActive;

    @Column(length = 5, nullable = false)
    private String retrospect;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static Alarm createAlarm(Member member) {
        return Alarm.builder()
                .diaryActive(true)
                .diary("20:00")
                .retrospectActive(true)
                .retrospect("20:00")
                .member(member)
                .build();
    }

    public void updateDiaryAlarm(ReqUpdateDiaryAlarm reqUpdateDiaryAlarm) {
        this.diaryActive = reqUpdateDiaryAlarm.getDiaryActive();
        this.diary = reqUpdateDiaryAlarm.getDiary();
    }

    public void updateRetrospectAlarm(ReqUpdateRetrospectAlarm reqUpdateRetrospectAlarm) {
        this.retrospectActive = reqUpdateRetrospectAlarm.getRetrospectActive();
        this.retrospect = reqUpdateRetrospectAlarm.getRetrospect();
    }
}
