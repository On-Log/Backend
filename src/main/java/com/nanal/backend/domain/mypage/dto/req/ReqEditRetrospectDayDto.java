package com.nanal.backend.domain.mypage.dto.req;

import lombok.Data;

import java.time.DayOfWeek;

@Data
public class ReqEditRetrospectDayDto {

    private DayOfWeek retrospectDay;
}
