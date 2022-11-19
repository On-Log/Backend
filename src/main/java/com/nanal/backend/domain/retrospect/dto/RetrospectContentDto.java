package com.nanal.backend.domain.retrospect.dto;

import com.nanal.backend.entity.RetrospectContent;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RetrospectContentDto {
    @Schema(description = "답변" , example = "~~~~했어~")
    String answer;

    @Schema(description = "회고 질문" , example = "이번주에 가장 많이 느꼈던 감정은 무엇인가요?")
    String question;

    public static RetrospectContentDto makeRetrospectContentDto(RetrospectContent retrospectContent) {
        RetrospectContentDto retrospectContentDto = new RetrospectContentDto();
        retrospectContentDto.setQuestion(retrospectContent.getQuestion());
        retrospectContentDto.setAnswer(retrospectContent.getAnswer());

        return retrospectContentDto;
    }
}
