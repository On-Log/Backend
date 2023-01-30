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
    List<Diary> findListByMemberAndWriteDate(Long memberId, LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = "SELECT d FROM Diary d WHERE d.member.memberId = :memberId AND (d.writeDate BETWEEN :startDate AND :endDate)")
    Optional<Diary> findDiaryByMemberAndWriteDate(Long memberId, LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = "SELECT * FROM diary d WHERE d.member_id = :memberId AND d.write_date LIKE :likeFormat", nativeQuery = true)
    List<Diary> findDiaryListByMemberAndWriteDate(Long memberId, String likeFormat);

    @Query(value = "SELECT * FROM diary d WHERE d.member_id = :memberId AND (DATE(write_date) BETWEEN :firstDate AND :lastDate) AND d.edit_status = :editStatus", nativeQuery = true)
    List<Diary> findListByMemberAndBetweenWriteDate(Long memberId, LocalDate firstDate, LocalDate lastDate, Boolean editStatus);

    @Modifying
    @Query(value = "UPDATE diary d SET d.edit_status = false WHERE d.edit_status = :editStatus AND d.write_date LIKE :likeFormat", nativeQuery = true)
    void updateEditStatusByWriteDate(Boolean editStatus, String likeFormat);

    @Modifying
    @Query(value = "UPDATE diary d SET d.edit_status = false WHERE d.member_id IN (:member_ids) AND (DATE(write_date) BETWEEN :firstDate AND :lastDate)", nativeQuery = true)
    void updateEditStatusByMemberAndBetweenWriteDate(@Param("member_ids") List<Long> member_ids, LocalDate firstDate, LocalDate lastDate);
}
