package com.nanal.backend.domain.diary.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReqGetCalendarDto {

    @NotNull(message = "fromDate 값이 올바르지 않습니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fromDate;

    @NotNull(message = "toDate 값이 올바르지 않습니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime toDate;

    @AssertTrue(message = "fromDate 는 toDate 보다 앞선 날짜여야 합니다.")
    public boolean isValidDate() {
        if(fromDate == null || toDate == null) return true;
        return this.fromDate.isBefore(this.toDate);
    }
}
