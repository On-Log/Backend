package com.nanal.backend.entity.log.repository;

import com.nanal.backend.entity.log.AuthLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthLogRepository extends JpaRepository<AuthLog, Long> {
}
