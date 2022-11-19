package com.nanal.backend.domain.retrospect.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class ReqEditRetroDto {
    @Schema(description = "회고 수정 날짜" , example = "2022-11-19T05:33:42.387Z")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime editDate;

    @NotBlank(message = "답변은 비어있을 수 없습니다.")
    @Schema(description = "답변" , example = "answerExample")
    @Size(max = 300, message="최대 300개의 문자만 입력 가능합니다.")
    private String answer;

    @NotBlank(message = "요일은 비어있을 수 없습니다.")
    @Schema(description = "week" , example = "3")
    private int week;

    @NotBlank(message = "질문 순번은 비어있을 수 없습니다.")
    @Schema(description = "질문 순번" , example = "2")
    private int index;
}