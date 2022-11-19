package com.nanal.backend.domain.mypage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;

@Data
public class ReqEditRetrospectDayDto {
    @Schema(description = "회고일" , example = "TUESDAY")
    @NotNull(message = "회고일은 비어있을 수 없습니다.")
    private DayOfWeek retrospectDay;
}
