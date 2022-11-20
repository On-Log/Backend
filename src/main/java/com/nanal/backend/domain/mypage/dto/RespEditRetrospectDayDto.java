package com.nanal.backend.domain.mypage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.DayOfWeek;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RespEditRetrospectDayDto {
    @Schema(description = "회고일" , example = "TUESDAY")
    private DayOfWeek userRetrospectDay;
}
