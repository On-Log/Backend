package com.nanal.backend.domain.mypage.entity;


import com.nanal.backend.global.config.BaseTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Feedback extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Long feedbackId;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false, length = 10)
    private String type;

    @Column(nullable = false, length = 500)
    private String content;
}
