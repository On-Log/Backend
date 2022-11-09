package com.nanal.backend.domain.diary.repository;

import com.nanal.backend.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
}
