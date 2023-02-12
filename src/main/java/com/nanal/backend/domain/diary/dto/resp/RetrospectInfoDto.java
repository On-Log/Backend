package com.nanal.backend.domain.diary.dto.resp;

import com.nanal.backend.domain.retrospect.entity.Retrospect;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class RetrospectInfoDto {

    private LocalDateTime retrospectDate;

    private String goal;

    public RetrospectInfoDto(Retrospect retrospect) {
        this.retrospectDate = retrospect.getWriteDate();
        this.goal = retrospect.getGoal();
    }
}
