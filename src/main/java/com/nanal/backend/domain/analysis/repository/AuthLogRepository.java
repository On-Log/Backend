package com.nanal.backend.domain.analysis.repository;

import com.nanal.backend.domain.analysis.dto.resp.DayDto;
import com.nanal.backend.domain.analysis.dto.resp.RespGetMauDto;
import com.nanal.backend.domain.analysis.dto.resp.WeekDto;
import com.nanal.backend.domain.analysis.entity.AuthLog;
import com.nanal.backend.domain.analysis.entity.RetrospectLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface AuthLogRepository extends JpaRepository<AuthLog, Long> {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    AuthLog save(AuthLog authLog);

//    @Query(value = "SELECT new com.nanal.backend.domain.analysis.dto.resp.DayDto(DAY(al.createdAt), COUNT(DISTINCT al.userEmail)) " +
//            "FROM AuthLog al " +
//            "WHERE al.createdAt >= :first AND al.createdAt < :last " +
//            "GROUP BY DAY(al.createdAt)")
//    List<DayDto> dauQuery(@Param("first")LocalDateTime first, @Param("last")LocalDateTime last);

    @Query(value = "SELECT new com.nanal.backend.domain.analysis.dto.resp.WeekDto(WEEK(al.createdAt) - WEEK(:first) + 1, COUNT(DISTINCT al.userEmail)) " +
            "FROM AuthLog al " +
            "WHERE al.createdAt >= :first AND al.createdAt < :last " +
            "GROUP BY WEEK(al.createdAt)")
    List<WeekDto> wauQuery(LocalDateTime first, LocalDateTime last);

    @Query(value = "SELECT new com.nanal.backend.domain.analysis.dto.resp.RespGetMauDto(MONTH(al.createdAt), COUNT(DISTINCT al.userEmail)) " +
            "FROM AuthLog al " +
            "WHERE al.createdAt >= :first AND al.createdAt < :last ")
    RespGetMauDto mauQuery(LocalDateTime first, LocalDateTime last);
}
