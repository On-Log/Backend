package com.nanal.backend.domain.retrospect.dto.resp;

import com.nanal.backend.domain.retrospect.entity.RetrospectContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RetrospectContentDto {

    @NotBlank(message = "question 은 비어있을 수 없습니다.")
    @Size(min = 1, max = 100, message="question은 최소 1개, 최대 100개의 문자만 입력 가능합니다.")
    String question;

    @NotBlank(message = "answer 는 비어있을 수 없습니다.")
    @Size(min = 1, max = 300, message="answer는 최소 1개, 최대 300개의 문자만 입력 가능합니다.")
    String answer;
    
    public RetrospectContentDto(RetrospectContent retrospectContent) {
        this.answer = retrospectContent.getAnswer();
        this.question = retrospectContent.getQuestion();
    }

    public static RetrospectContentDto makeRetrospectContentDto(RetrospectContent retrospectContent) {
        RetrospectContentDto retrospectContentDto = new RetrospectContentDto();
        retrospectContentDto.setQuestion(retrospectContent.getQuestion());
        retrospectContentDto.setAnswer(retrospectContent.getAnswer());

        return retrospectContentDto;
    }
}
