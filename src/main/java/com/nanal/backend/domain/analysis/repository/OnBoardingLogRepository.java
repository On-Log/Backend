package com.nanal.backend.domain.analysis.repository;

import com.nanal.backend.domain.analysis.entity.OnBoardingLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OnBoardingLogRepository extends JpaRepository<OnBoardingLog, Long> {
}
