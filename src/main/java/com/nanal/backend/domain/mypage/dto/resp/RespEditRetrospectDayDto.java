package com.nanal.backend.domain.mypage.dto.resp;

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
    private DayOfWeek updatedRetrospectDay;
}
