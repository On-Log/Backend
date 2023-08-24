package com.nanal.backend.domain.alarm.repository;

import com.nanal.backend.domain.alarm.entity.Alarm;
import com.nanal.backend.domain.auth.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    Optional<Alarm> findByMember(Member member);
}
