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

    @NotNull(message = "retrospectId은 비어있을 수 없습니다.")
    private Long retrospectId;

}