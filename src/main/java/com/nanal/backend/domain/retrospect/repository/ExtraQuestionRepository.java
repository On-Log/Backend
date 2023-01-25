package com.nanal.backend.domain.retrospect.repository;

import com.nanal.backend.domain.retrospect.entity.ExtraQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExtraQuestionRepository extends JpaRepository<ExtraQuestion, Long> {
    @Query(value = "SELECT * FROM extra_question q WHERE q.goal_id = :goalId", nativeQuery = true)
    List<ExtraQuestion> findListByGoal(Long goalId);
}
