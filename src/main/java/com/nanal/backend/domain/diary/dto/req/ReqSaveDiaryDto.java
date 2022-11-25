package com.nanal.backend.domain.diary.dto.req;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReqSaveDiaryDto {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime date;

    @NotBlank(message = "content 는 비어있을 수 없습니다.")
    @Size(max = 300, message="content 는 최대 300개의 문자만 입력 가능합니다.")
    private String content;

    @Valid
    private List<KeywordDto> keywords;

}
