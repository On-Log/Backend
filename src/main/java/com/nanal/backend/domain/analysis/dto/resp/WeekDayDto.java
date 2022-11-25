package com.nanal.backend.domain.analysis.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WeekDayDto {
    private String day;

    private Long userCount;
}
