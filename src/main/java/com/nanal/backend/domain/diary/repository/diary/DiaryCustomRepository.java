package com.nanal.backend.domain.diary.repository.diary;

import com.nanal.backend.domain.diary.entity.Diary;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DiaryCustomRepository {

    List<LocalDateTime> findExistDiaryDateList(Long memberId, LocalDateTime fromDate, LocalDateTime toDate);

    void checkTodayDiaryAlreadyExist(Long memberId, LocalDateTime today);

    Diary findDiary(Long memberId, LocalDateTime date);

    List<Diary> findDiaryListByMemberAndWriteDate(Long memberId, LocalDateTime fromDate, LocalDateTime toDate);

    Optional<Diary> findDiaryByMemberAndWriteDate(Long memberId, LocalDateTime fromDate, LocalDateTime toDate);
}
