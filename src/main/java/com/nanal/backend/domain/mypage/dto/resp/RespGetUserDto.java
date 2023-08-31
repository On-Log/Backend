package com.nanal.backend.domain.mypage.dto.resp;

import com.nanal.backend.domain.alarm.entity.Alarm;
import com.nanal.backend.domain.auth.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.Optional;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RespGetUserDto {
    private String nickname;

    private String email;

    private DayOfWeek retrospectDay;

    private Boolean diaryAlarmActive;

    private String diaryAlarmTime;

    private Boolean retrospectAlarmActive;

    private String retrospectAlarmTime;

    private String goods;

    public static RespGetUserDto createRespGetUserDto(Member member) {
        Alarm alarm = member.getAlarm();
        return RespGetUserDto.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .retrospectDay(member.getRetrospectDay())
                .diaryAlarmActive(alarm.getDiaryActive())
                .diaryAlarmTime(alarm.getDiaryTime())
                .retrospectAlarmActive(alarm.getRetrospectActive())
                .retrospectAlarmTime(alarm.getRetrospectTime())
                .goods(member.getGoods() != null ? member.getGoods() : null)
                .build();
    }
}
