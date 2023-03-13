package com.nanal.backend.domain.diary.repository;

import com.nanal.backend.domain.diary.entity.Diary;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DiaryCustomRepository {

    List<LocalDateTime> getExistDiaryDateList(Long memberId, LocalDateTime fromDate, LocalDateTime toDate);

    void checkTodayDiaryAlreadyExist(Long memberId, LocalDateTime today);

    Diary findDiary(Long memberId, LocalDateTime date);

    List<Diary> findDiaryListByMemberAndWriteDate(Long memberId, LocalDateTime fromDate, LocalDateTime toDate);

    Optional<Diary> findDiaryByMemberAndWriteDate(Long memberId, LocalDateTime fromDate, LocalDateTime toDate);

    List<Diary> findListByMemberAndBetweenWriteDate(Long memberId, LocalDate firstDate, LocalDate lastDate, Boolean editStatus);
}
