package com.nanal.backend.domain.retrospect.repository;

import com.nanal.backend.domain.retrospect.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(value = "SELECT * FROM question q WHERE q.goal_id = :goalId", nativeQuery = true)
    List<Question> findListByGoal(Long goalId);


}