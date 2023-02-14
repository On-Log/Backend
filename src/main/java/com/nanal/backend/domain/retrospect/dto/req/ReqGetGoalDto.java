package com.nanal.backend.domain.retrospect.dto.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ReqGetGoalDto {

    @NotNull(message = "goalIndex는 비어있을 수 없습니다.")
    private Long goalIndex;
}
