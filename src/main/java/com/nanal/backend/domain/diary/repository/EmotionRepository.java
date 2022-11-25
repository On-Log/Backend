package com.nanal.backend.domain.diary.repository;

import com.nanal.backend.domain.diary.entity.Emotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmotionRepository extends JpaRepository<Emotion, Long> {
}
