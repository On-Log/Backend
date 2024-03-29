package com.nanal.backend.domain.excel.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WauDto {
    private Integer week;
    private Long userCount;
    private LocalDateTime weekStartDate;
}
