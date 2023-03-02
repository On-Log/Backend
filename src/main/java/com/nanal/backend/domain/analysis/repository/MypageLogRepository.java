package com.nanal.backend.domain.analysis.repository;

import com.nanal.backend.domain.analysis.entity.DiaryLog;
import com.nanal.backend.domain.analysis.entity.MypageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface MypageLogRepository extends JpaRepository<MypageLog, Long> {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    MypageLog save(MypageLog mypageLog);
}
