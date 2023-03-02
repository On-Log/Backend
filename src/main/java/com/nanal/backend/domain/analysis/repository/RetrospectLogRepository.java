package com.nanal.backend.domain.analysis.repository;

import com.nanal.backend.domain.analysis.dto.resp.WeekDayDao;
import com.nanal.backend.domain.analysis.entity.DiaryLog;
import com.nanal.backend.domain.analysis.entity.MypageLog;
import com.nanal.backend.domain.analysis.entity.RetrospectLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface RetrospectLogRepository extends JpaRepository<RetrospectLog, Long> {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    RetrospectLog save(RetrospectLog retrospectLog);

    @Query(value = "SELECT new com.nanal.backend.domain.analysis.dto.resp.WeekDto(DAYOFWEEK(rl.createdAt), COUNT(rl.retrospectLogId)) " +
            "FROM RetrospectLog rl " +
            "WHERE rl.serviceName = :serviceName AND rl.createdAt >= :first AND rl.createdAt < :last " +
            "GROUP BY DAYOFWEEK(rl.createdAt)")
    List<WeekDayDao> weekDayRetrospectQuery(String serviceName, LocalDateTime first, LocalDateTime last);

    /*
    한달기준 요일별
    @Query(value = "SELECT new com.nanal.backend.domain.analysis.dto.resp.WeekDto(DAYOFWEEK(rl.createdAt), COUNT(DISTINCT rl.userEmail)) " +
            "FROM RetrospectLog rl " +
            "WHERE rl.createdAt >= :first AND rl.createdAt < :last " +
            "GROUP BY DAYOFWEEK(rl.createdAt)")
    List<WeekDayDto> weekDayRetrospectQuery(LocalDateTime first, LocalDateTime last);

     */
}
