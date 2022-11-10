package com.nanal.backend.domain.diary.repository;

import com.nanal.backend.entity.Emotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmotionRepository extends JpaRepository<Emotion, Long> {
}
