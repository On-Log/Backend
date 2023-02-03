package com.nanal.backend.domain.diary.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReqDiaryDto {

    @NotNull(message = "date 값이 올바르지 않습니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime date;

    @NotBlank(message = "content 는 비어있을 수 없습니다.")
    @Size(min = 1, max = 300, message="content 는 최대 300개의 문자만 입력 가능합니다.")
    private String content;

    @Valid
    @Size(min = 1, max = 5, message ="keyword 의 개수는 최소 1개, 최대 5개입니다.")
    private List<KeywordDto> keywords;
}
