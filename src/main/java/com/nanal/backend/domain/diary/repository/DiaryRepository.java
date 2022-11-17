package com.nanal.backend.domain.diary.repository;

import com.nanal.backend.entity.Diary;
import com.nanal.backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    //@Query(value = "SELECT write_date FROM diary WHERE member_id = :memberId AND write_date >= :firstDate AND write_date <= :lastDate;", nativeQuery = true)
    //List<LocalDateTime> findByMemberAndWriteDate(Long memberId, LocalDate firstDate, LocalDate lastDate);
    @Query(value = "SELECT * FROM diary d WHERE d.member_id = :memberId AND d.write_date LIKE :likeFormat", nativeQuery = true)
    List<Diary> findListByMemberAndWriteDate(Long memberId, String likeFormat);

    @Query(value = "SELECT * FROM diary d WHERE d.member_id = :memberId AND d.write_date LIKE :likeFormat", nativeQuery = true)
    Diary findDiaryByMemberAndWriteDate(Long memberId, String likeFormat);

    @Modifying
    @Query(value = "DELETE FROM Diary d WHERE d.member.memberId = :memberId AND d.writeDate = :writeDate")
    void deleteByMemberAndWriteDate(Long memberId, LocalDateTime writeDate);

    @Query(value = "SELECT * FROM diary d WHERE d.member_id = :memberId AND (DATE(write_date) BETWEEN :firstDate AND :lastDate)", nativeQuery = true)
    List<Diary> findListByMemberBetweenWriteDate(Long memberId, LocalDate firstDate, LocalDate lastDate);

    //List<LocalDateTime> findByMemberIdAndAllByWriteDateBetween(Long memberId, LocalDate firstDate, LocalDate lastDate);
}
