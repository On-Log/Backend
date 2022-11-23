package com.nanal.backend.domain.analysis.repository;

import com.nanal.backend.entity.log.DiaryLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryLogRepository extends JpaRepository<DiaryLog, Long> {
}
