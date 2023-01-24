package com.nanal.backend.domain.mypage.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReqEditRetrospectDayDto {

    private DayOfWeek retrospectDay;
}
