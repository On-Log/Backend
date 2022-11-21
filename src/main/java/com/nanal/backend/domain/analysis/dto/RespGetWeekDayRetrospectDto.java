package com.nanal.backend.domain.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RespGetWeekDayRetrospectDto {

    private List<WeekDayDto> weekDayDtoList;
}
