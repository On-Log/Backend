package com.nanal.backend.domain.mypage.dto;

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
    private DayOfWeek userRetrospectDay;
}
