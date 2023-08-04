package com.nanal.backend.domain.analysis.repository;

import com.nanal.backend.domain.analysis.dto.resp.DayDto;
import com.nanal.backend.domain.analysis.entity.DiaryLog;
import com.nanal.backend.domain.excel.dto.resp.DauDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


public interface DiaryLogRepository extends JpaRepository<DiaryLog, Long> {

    @Query(value = "SELECT new com.nanal.backend.domain.analysis.dto.resp.DayDto(DAY(dl.createdAt), COUNT(DISTINCT dl.userEmail)) " +
            "FROM DiaryLog dl " +
            "WHERE dl.createdAt >= :from AND dl.createdAt < :to AND dl.serviceName = 'getCalendar' "+
            "GROUP BY DAY(dl.createdAt)")
    List<DayDto> dauQuery(LocalDateTime from, LocalDateTime to);

    @Query(value = "SELECT new com.nanal.backend.domain.excel.dto.resp.DauDto(dl.createdAt, COUNT(DISTINCT dl.userEmail)) " +
            "FROM DiaryLog dl " +
            "WHERE dl.createdAt >= :from AND dl.createdAt < :to AND dl.serviceName = 'getCalendar' "+
            "GROUP BY dl.createdAt")
    List<DauDto> excelDauQuery(LocalDateTime from, LocalDateTime to);

    @Query("SELECT dl FROM DiaryLog dl WHERE dl.createdAt >= :from AND dl.createdAt < :to AND dl.serviceName = 'getCalendar'")
    List<DiaryLog> findLogsBetweenDates(LocalDateTime from, LocalDateTime to);

    @Query(value = "SELECT COUNT(DISTINCT dl.userEmail) " +
            "FROM DiaryLog dl " +
            "WHERE dl.createdAt >= :from AND dl.createdAt < :to AND dl.serviceName = 'writeDiary'")
    Integer diaryDAU(LocalDateTime from, LocalDateTime to);

    @Query(value = "SELECT COUNT(DISTINCT dl.userEmail) " +
            "FROM DiaryLog dl " +
            "WHERE dl.createdAt >= :from AND dl.createdAt < :to AND dl.serviceName = 'getCalendar'")
    Integer loginDAU(LocalDateTime from, LocalDateTime to);

    @Transactional
    DiaryLog save(DiaryLog diaryLog);
}
