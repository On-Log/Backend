package com.nanal.backend.domain.mypage;

import com.nanal.backend.domain.mypage.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
