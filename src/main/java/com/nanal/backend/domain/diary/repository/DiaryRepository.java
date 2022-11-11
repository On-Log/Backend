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
    @Query(value = "SELECT * FROM diary WHERE member_id = :memberId AND write_date LIKE :likeFormat", nativeQuery = true)
    List<Diary> findByMemberAndWriteDate(Long memberId, String likeFormat);

    @Modifying
    @Query(value = "DELETE FROM diary WHERE member_id = :memberId AND write_date LIKE :likeFormat", nativeQuery = true)
    void deleteByMemberAndWriteDate(Long memberId, String likeFormat);

    //List<LocalDateTime> findByMemberIdAndAllByWriteDateBetween(Long memberId, LocalDate firstDate, LocalDate lastDate);
}
