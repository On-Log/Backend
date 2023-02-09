package com.nanal.backend.domain.retrospect.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReqEditRetroDto {
    @NotNull(message = "editDate 값이 올바르지 않습니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime editDate;

    @NotBlank(message = "answer 는 비어있을 수 없습니다.")
    @Size(max = 300, message="answer 는 최대 300개의 문자만 입력 가능합니다.")
    private String answer;

    @NotNull(message = "week은 비어있을 수 없습니다.")
    private Integer week;

    @NotNull(message = "index는 비어있을 수 없습니다.")
    private Integer index;
}