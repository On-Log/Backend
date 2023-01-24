package com.nanal.backend.domain.mypage.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RespCheckChangeAvailability {
    LocalDateTime nextChangeableDate;

    DayOfWeek curRetrospectDay;
}
