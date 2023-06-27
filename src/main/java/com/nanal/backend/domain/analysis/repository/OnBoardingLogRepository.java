package com.nanal.backend.domain.analysis.repository;

import com.nanal.backend.domain.analysis.entity.AuthLog;
import com.nanal.backend.domain.analysis.entity.OnBoardingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface OnBoardingLogRepository extends JpaRepository<OnBoardingLog, Long> {

    @Transactional
    OnBoardingLog save(OnBoardingLog onBoardingLog);
}
