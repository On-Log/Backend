package com.nanal.backend.domain.diary.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReqSaveDiaryDto {

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;

    @NotNull(message = "내용은 비어있을 수 없습니다.")
    //@Max(value = 300, message="최대 300개의 문자만 입력 가능합니다.")
    private String content;

    @NotNull(message = "키워드는 비어있을 수 없습니다.")
    private List<KeywordDto> keywords;

}
