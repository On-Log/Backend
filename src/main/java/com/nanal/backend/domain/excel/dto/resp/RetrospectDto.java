package com.nanal.backend.domain.excel.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RetrospectDto {

    private Integer month;

    private String UserEmail;

    private Long writeCount;

}
