package com.nanal.backend.global.config;

import com.nanal.backend.domain.diary.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RequiredArgsConstructor
@Component
public class SchedulingConfig {

    private final DiaryRepository diaryRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void setDiaryEditStatus() {

        log.info("스케쥴러 실행");
        // 1주일 이전의 일기 수정 불가 처리.
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime lastWeek = currentTime.minusDays(7);
        String yearMonthDay = lastWeek.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "%";

        diaryRepository.updateEditStatusByWriteDate(true, yearMonthDay);

        // 이전날이 회고요일 이였을 경우, 이전날-6 ~ 이전날 까지의 일기 수정 불가 처리.
    }
}
