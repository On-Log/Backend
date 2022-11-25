package com.nanal.backend.domain.analysis.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WeekDto {
    private Integer week;

    private Long userCount;
}
