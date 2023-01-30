package com.nanal.backend.domain.diary.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReqDeleteDiaryDto {

    @NotNull(message = "date 는 비어있을 수 없습니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime date;
}
