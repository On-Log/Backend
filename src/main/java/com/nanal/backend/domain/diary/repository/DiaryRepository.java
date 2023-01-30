package com.nanal.backend.domain.diary.repository;

import com.nanal.backend.domain.diary.entity.Diary;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    @Query(value = "SELECT d FROM Diary d WHERE d.member.memberId = :memberId AND (d.writeDate BETWEEN :startDate AND :endDate)")
    List<Diary> findDiaryListByMemberAndWriteDate(Long memberId, LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = "SELECT d FROM Diary d WHERE d.member.memberId = :memberId AND (d.writeDate BETWEEN :startDate AND :endDate)")
    Optional<Diary> findDiaryByMemberAndWriteDate(Long memberId, LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = "SELECT * FROM diary d WHERE d.member_id = :memberId AND (DATE(write_date) BETWEEN :firstDate AND :lastDate) AND d.edit_status = :editStatus", nativeQuery = true)
    List<Diary> findListByMemberAndBetweenWriteDate(Long memberId, LocalDate firstDate, LocalDate lastDate, Boolean editStatus);

    @Modifying
    @Query(value = "UPDATE Diary d SET d.editStatus = false WHERE d.editStatus = :editStatus AND (d.writeDate BETWEEN :startDate AND :endDate)")
    void updateEditStatusByWriteDate(Boolean editStatus, LocalDateTime startDate, LocalDateTime endDate);

    @Modifying
    @Query(value = "UPDATE Diary d SET d.editStatus = false WHERE d.member.memberId IN (:member_ids) AND (d.writeDate BETWEEN :startDate AND :endDate)")
    void updateEditStatusByMemberAndBetweenWriteDate(@Param("member_ids") List<Long> member_ids, LocalDateTime startDate, LocalDateTime endDate);
}
