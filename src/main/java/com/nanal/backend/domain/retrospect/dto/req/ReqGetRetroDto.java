package com.nanal.backend.domain.retrospect.dto.req;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class ReqGetRetroDto {

    @NotNull(message = "fromDate 값이 올바르지 않습니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fromDate;

    @NotNull(message = "toDate 값이 올바르지 않습니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime toDate;

    @NotNull(message = "week 은 비어있을 수 없습니다.")
    private Integer week;
}
