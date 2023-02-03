package com.nanal.backend.domain.retrospect.dto.req;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class ReqGetRetroDto {

    //선택한 월
    @NotNull(message = "selectDate 는 비어있을 수 없습니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime selectDate;

    @NotBlank(message = "week 은 비어있을 수 없습니다.")
    private int week;
}
