package com.nanal.backend.domain.diary.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReqEditDiaryDto {
    @NotNull(message = "내용은 비어있을 수 없습니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime editDate;

    @Schema(description = "내용" , example = "오늘은 카페 알바가 있는 날이었는데, 정말 바빴다. 평소엔 여유로운 편이라, 오늘도 틈틈이 할 일을 하려했는데.. 완전 실패다. 낮에 쪽잠이라도 자두는 건데.. 바쁘다 바빠.요새 잠도 제대로 못 자고 끼니도 거르다보니, 살도 쭉쭉 빠지는 듯 하다. 근데... 뭐.... 나쁘지 않아? ㅎ..... ")
    @NotNull(message = "내용은 비어있을 수 없습니다.")
    @Size(max = 300, message="최대 300개의 문자만 입력 가능합니다.")
    private String content;

    @NotNull(message = "키워드는 비어있을 수 없습니다.")
    private List<KeywordDto> keywords;
}
