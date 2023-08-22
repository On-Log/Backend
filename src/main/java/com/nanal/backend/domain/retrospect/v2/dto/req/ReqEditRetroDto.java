package com.nanal.backend.domain.retrospect.v2.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReqEditRetroDto {

    @NotBlank(message = "answer 는 비어있을 수 없습니다.")
    @Size(min = 1, max = 300, message="answer 는 최소 1개, 최대 300개의 문자만 입력 가능합니다.")
    private String answer;

    @NotNull(message = "retrospectId은 비어있을 수 없습니다.")
    private Long retrospectId;

    @NotNull(message = "index는 비어있을 수 없습니다.")
    private Integer index;

}