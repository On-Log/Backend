package com.nanal.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Table(name = "emotion")
@Entity
public class Emotion {

    @Id
    @GeneratedValue
    @Column(name = "emotion_id")
    private Long emotionId;

    @Column(length = 20, nullable = false)
    private String emotion;
}
