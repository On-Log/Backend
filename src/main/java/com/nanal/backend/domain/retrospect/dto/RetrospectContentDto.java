package com.nanal.backend.domain.retrospect.dto;

import com.nanal.backend.domain.retrospect.entity.RetrospectContent;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RetrospectContentDto {
    @NotBlank(message = "answer 는 비어있을 수 없습니다.")
    @Size(max = 300, message="answer 는 최대 300개의 문자만 입력 가능합니다.")
    String answer;

    @NotBlank(message = "question 은 비어있을 수 없습니다.")
    @Size(max = 100, message="question 은 최대 100개의 문자만 입력 가능합니다.")
    String question;

    public static RetrospectContentDto makeRetrospectContentDto(RetrospectContent retrospectContent) {
        RetrospectContentDto retrospectContentDto = new RetrospectContentDto();
        retrospectContentDto.setQuestion(retrospectContent.getQuestion());
        retrospectContentDto.setAnswer(retrospectContent.getAnswer());

        return retrospectContentDto;
    }
}
