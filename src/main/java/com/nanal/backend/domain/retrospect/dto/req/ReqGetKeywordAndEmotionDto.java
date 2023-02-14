package com.nanal.backend.domain.retrospect.dto.req;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class ReqGetKeywordAndEmotionDto {

    //현재 시간
    @NotNull(message = "currentDate 값이 올바르지 않습니다..")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime currentDate;
}
