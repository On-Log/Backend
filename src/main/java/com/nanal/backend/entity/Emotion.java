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

    @Column(length = 20, nullable = false)
    private String emotion;
}
