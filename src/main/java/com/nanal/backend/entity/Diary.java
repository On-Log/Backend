package com.nanal.backend.entity;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Table(name = "diary")
@Entity
public class Diary {

    @Id @GeneratedValue
    @Column(name = "diary_id")
    private Long diaryId;

    @Column(length = 300, nullable = true)
    private String diaryContent;

    private LocalDateTime writeDate;

    private Boolean editStatus;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
