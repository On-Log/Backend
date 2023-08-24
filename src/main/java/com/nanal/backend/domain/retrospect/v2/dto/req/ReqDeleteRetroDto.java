package com.nanal.backend.domain.retrospect.v2.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReqDeleteRetroDto {
    @NotNull(message = "retrospectId은 비어있을 수 없습니다.")
    private Long retrospectId;
}
