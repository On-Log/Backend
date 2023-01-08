package com.nanal.backend.domain.retrospect.entity;

import com.nanal.backend.global.config.BaseTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Table(name = "question")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Question extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long questionId;

    @Column(length = 200, nullable = false)
    private String content;

    @Column(length = 500, nullable = false)
    private String help;

    @Column(name = "goal_id")
    private Long goalId;
}
