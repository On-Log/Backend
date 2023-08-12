package com.nanal.backend.domain.retrospect.dto.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ReqGetRetroDto {

    @NotNull(message = "retrospectId은 비어있을 수 없습니다.")
    private Long retrospectId;

}
