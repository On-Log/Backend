package com.nanal.backend.domain.diary.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class RespGetCalendarDto {

    List<Integer> existDiaryDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Integer prevRetroDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Integer postRetroDate;
}
