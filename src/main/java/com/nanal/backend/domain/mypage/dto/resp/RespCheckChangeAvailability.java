package com.nanal.backend.domain.mypage.dto.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    LocalDateTime nextChangeableDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    DayOfWeek curRetrospectDay;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    LocalDateTime changeableDate;

    static public RespCheckChangeAvailability changeable(LocalDateTime nextChangeableDate, DayOfWeek curRetrospectDay) {
        return RespCheckChangeAvailability.builder()
                .nextChangeableDate(nextChangeableDate)
                .curRetrospectDay(curRetrospectDay)
                .build();
    }

    static public RespCheckChangeAvailability unchangeable(LocalDateTime changeableDate) {
        return RespCheckChangeAvailability.builder()
                .changeableDate(changeableDate)
                .build();
    }
}
