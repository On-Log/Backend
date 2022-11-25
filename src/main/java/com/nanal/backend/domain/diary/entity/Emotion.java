package com.nanal.backend.domain.diary.entity;

import com.nanal.backend.global.config.BaseTime;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Table(name = "emotion")
@Entity
public class Emotion extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emotion_id")
    private Long emotionId;

    @Column(nullable = false, length = 5)
    private String emotion;
}
