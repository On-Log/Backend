package com.nanal.backend.entity;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Table(name = "emotion")
@Entity
public class Emotion {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emotion_id")
    private Long emotionId;

    @Column(nullable = false, length = 5)
    private String emotion;
}
