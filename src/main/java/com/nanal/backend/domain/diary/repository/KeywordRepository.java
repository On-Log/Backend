package com.nanal.backend.domain.diary.repository;

import com.nanal.backend.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
}
