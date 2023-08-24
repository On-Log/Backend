package com.nanal.backend.domain.search.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReqSearchDto {

    @Pattern(regexp = "^[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9]*$", message = "특수문자를 포함할 수 없습니다.")
    private String searchWord;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;

    @Min(value = 0, message = "offset은 음수일 수 없습니다.")
    private Integer offset = 0;

    @Min(value = 1, message = "limit는 1 이상 10 이하의 값만 가능합니다.")
    @Max(value = 10, message = "limit는 1 이상 10 이하의 값만 가능합니다.")
    private Integer limit = 10;
}
