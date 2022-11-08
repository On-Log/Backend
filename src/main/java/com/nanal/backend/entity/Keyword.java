package com.nanal.backend.entity;


import lombok.Getter;

import javax.persistence.*;

@Getter
@Table(name = "keyword")
@Entity
public class Keyword {

    @Id
    @GeneratedValue
    @Column(name = "keyword_id")
    private Long keywordId;

    @Column(length = 30, nullable = false)
    private String keyword;

    @ManyToOne
    @JoinColumn(name = "diary_id")
    private Diary diary;

}
