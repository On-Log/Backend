package com.nanal.backend.domain.analysis.repository;

import com.nanal.backend.domain.analysis.entity.MypageLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MypageLogRepository extends JpaRepository<MypageLog, Long> {
}
