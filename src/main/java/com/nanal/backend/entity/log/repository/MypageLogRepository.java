package com.nanal.backend.entity.log.repository;

import com.nanal.backend.entity.log.MypageLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MypageLogRepository extends JpaRepository<MypageLog, Long> {
}
