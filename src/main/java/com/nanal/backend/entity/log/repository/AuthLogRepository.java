package com.nanal.backend.entity.log.repository;

import com.nanal.backend.domain.analysis.dto.DayDto;
import com.nanal.backend.entity.log.AuthLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AuthLogRepository extends JpaRepository<AuthLog, Long> {

    @Query(value = "SELECT new com.nanal.backend.domain.analysis.dto.DayDto(DAY(al.createdAt), COUNT(DISTINCT al.userEmail)) " +
            "FROM AuthLog al " +
            "WHERE al.createdAt >= :first AND al.createdAt < :last " +
            "GROUP BY DAY(al.createdAt)")
    List<DayDto> dauQuery(@Param("first") LocalDateTime first, @Param("last") LocalDateTime last);
}
