package com.nanal.backend.domain.diary.dto.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RespGetCalendarDto {

    private String nickname;

    private Boolean isRetrospectDay;

    private List<LocalDateTime> existDiaryDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime nextDayOfPrevRetroDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime retroDate;

    private List<RetrospectInfoDto> retrospectInfoList;
}
