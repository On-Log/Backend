package com.nanal.backend.global.config;

import com.nanal.backend.domain.diary.repository.DiaryRepository;
import com.nanal.backend.domain.auth.repository.MemberRepository;
import com.nanal.backend.domain.retrospect.repository.RetrospectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class SchedulingConfig {

    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;
    private final RetrospectRepository retrospectRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void setDiaryEditStatus() {

        LocalDateTime currentTime = LocalDateTime.now();

        log.info("[Scheduling] 1주일 이전의 일기 수정 불가 처리");
        LocalDate lastWeek = currentTime.minusDays(7).toLocalDate();
        diaryRepository.updateEditStatusByWriteDate(true, lastWeek.atStartOfDay(), lastWeek.atTime(LocalTime.MAX));
    }

    @Transactional
    @Scheduled(cron = "0 0 7 * * *")
    public void setRetroEditStatus() {

        LocalDateTime currentTime = LocalDateTime.now();

        log.info("[Scheduling] 이전날이 회고요일 이였을 경우, 회고 수정 불가 처리");
        DayOfWeek prevDay = currentTime.minusDays(1).getDayOfWeek();
        // 이전날이 회고요일인 memberId 조회
        List<Long> memberIds = memberRepository.findMemberIdByRetrospectDay(prevDay);
        retrospectRepository.updateEditStatusByMember(memberIds);

        log.info("[Scheduling] 이전날이 회고요일 이였을 경우, 이전날-6 ~ 이전날 까지의 일기 수정 불가 처리");
        diaryRepository.updateEditStatusByMemberAndBetweenWriteDate(memberIds, currentTime.minusDays(7), currentTime.minusDays(1));
    }
}
