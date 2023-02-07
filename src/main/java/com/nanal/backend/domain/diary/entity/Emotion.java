package com.nanal.backend.domain.diary.entity;

import com.nanal.backend.global.config.BaseTime;
import lombok.Getter;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Getter
@Table(name = "emotion")
@Entity
public class Emotion extends BaseTime {

    public static final List<String> EMOTIONS = Arrays.asList("행복", "여유", "슬픔", "복잡", "즐거움",
                                                              "의욕", "쏘쏘", "안심", "아쉬움", "화남",
                                                              "기대", "놀람", "외로움", "짜증", "힘듦",
                                                              "뿌듯", "상쾌", "불안", "부담", "피곤");

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emotion_id")
    private Long emotionId;

    @Column(nullable = false, length = 5)
    private String emotion;
}
