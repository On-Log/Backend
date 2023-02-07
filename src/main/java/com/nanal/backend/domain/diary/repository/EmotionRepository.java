package com.nanal.backend.domain.diary.repository;

import com.nanal.backend.domain.diary.entity.Emotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmotionRepository extends JpaRepository<Emotion, Long> {

    @Query("select e from Emotion e where e.emotion in :emotions")
    List<Emotion> findEmotionsIn(List<String> emotions);
}
