package com.nanal.backend.domain.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReqSearchDto {
    @NotBlank
    private String searchWord;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Integer offset;

    private Integer limit;
}
