package com.nanal.backend.domain.diary.domain;

import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DiaryWritableWeek {

    private LocalDateTime nextDayOfPrevRetroDate;

    private LocalDateTime retroDate;

    public static DiaryWritableWeek create(DayOfWeek retrospectDay, LocalDateTime now) {
        return DiaryWritableWeek.builder()
                .nextDayOfPrevRetroDate(getNextDayOfPrevRetroDate(retrospectDay, now))
                .retroDate(getRetroDate(retrospectDay, now))
                .build();
    }

    public static LocalDateTime getNextDayOfPrevRetroDate(DayOfWeek retrospectDay, LocalDateTime now) {
        // 이전 회고일
        LocalDateTime prevRetroDate = now.with(TemporalAdjusters.previous(retrospectDay)).with(LocalTime.MIN);

        // 해당 주는 이전 회고일 다음날부터 다음 회고 일까지이므로 '이전 회고일 + 1' 을 해줘야함
        return prevRetroDate.plusDays(1);
    }

    public static LocalDateTime getRetroDate(DayOfWeek retrospectDay, LocalDateTime now) {
        // 다음 회고일
        return now.with(TemporalAdjusters.nextOrSame(retrospectDay)).with(LocalTime.MIN);
    }
}
