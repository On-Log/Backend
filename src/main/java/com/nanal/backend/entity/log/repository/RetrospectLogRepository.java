package com.nanal.backend.entity.log.repository;

import com.nanal.backend.domain.analysis.dto.WeekDayDao;
import com.nanal.backend.entity.log.RetrospectLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface RetrospectLogRepository extends JpaRepository<RetrospectLog, Long> {

    @Query(value = "SELECT new com.nanal.backend.domain.analysis.dto.WeekDto(DAYOFWEEK(rl.createdAt), COUNT(rl.retrospectLogId)) " +
            "FROM RetrospectLog rl " +
            "WHERE rl.serviceName = :serviceName AND rl.createdAt >= :first AND rl.createdAt < :last " +
            "GROUP BY DAYOFWEEK(rl.createdAt)")
    List<WeekDayDao> weekDayRetrospectQuery(String serviceName, LocalDateTime first, LocalDateTime last);

    /*
    한달기준 요일별
    @Query(value = "SELECT new com.nanal.backend.domain.analysis.dto.WeekDto(DAYOFWEEK(rl.createdAt), COUNT(DISTINCT rl.userEmail)) " +
            "FROM RetrospectLog rl " +
            "WHERE rl.createdAt >= :first AND rl.createdAt < :last " +
            "GROUP BY DAYOFWEEK(rl.createdAt)")
    List<WeekDayDto> weekDayRetrospectQuery(LocalDateTime first, LocalDateTime last);

     */
}
