package com.nanal.backend.domain.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;

@Data
public class ReqEditRetrospectDayDto {
    @NotNull(message = "회고일은 비어있을 수 없습니다.")
    private DayOfWeek retrospectDay;
}
