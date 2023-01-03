package com.nanal.backend.domain.mypage.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.DayOfWeek;

@Data
public class ReqEditRetrospectDayDto {

    private DayOfWeek retrospectDay;
}
