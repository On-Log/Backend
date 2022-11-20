package com.nanal.backend.entity.log.repository;

import com.nanal.backend.entity.log.RetrospectLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RetrospectLogRepository extends JpaRepository<RetrospectLog, Long> {
}
