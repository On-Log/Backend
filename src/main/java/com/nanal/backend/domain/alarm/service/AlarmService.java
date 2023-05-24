package com.nanal.backend.domain.alarm.service;

import com.nanal.backend.domain.alarm.dto.ReqUpdateDiaryAlarm;
import com.nanal.backend.domain.alarm.dto.ReqUpdateRetrospectAlarm;
import com.nanal.backend.domain.alarm.entity.Alarm;
import com.nanal.backend.domain.alarm.repository.AlarmRepository;
import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.domain.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class AlarmService {

    private final MemberRepository memberRepository;

    public void updateDiaryAlarm(String socialId, ReqUpdateDiaryAlarm reqUpdateDiaryAlarm) {
        // socialId 로 유저 조회
        Member member = memberRepository.findMember(socialId);

        Alarm alarm = member.getAlarm();

        System.out.println(reqUpdateDiaryAlarm.getDiaryAlarmActive());
        System.out.println(reqUpdateDiaryAlarm.getDiaryAlarmTime());

        alarm.updateDiaryAlarm(reqUpdateDiaryAlarm);
    }

    public void updateRetrospectAlarm(String socialId, ReqUpdateRetrospectAlarm reqUpdateRetrospectAlarm) {
        // socialId 로 유저 조회
        Member member = memberRepository.findMember(socialId);

        Alarm alarm = member.getAlarm();

        alarm.updateRetrospectAlarm(reqUpdateRetrospectAlarm);
    }
}
