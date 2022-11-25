package com.nanal.backend.domain.analysis.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
public class RespGetMauDto {
    private Integer month;

    private Long userCount;
}
