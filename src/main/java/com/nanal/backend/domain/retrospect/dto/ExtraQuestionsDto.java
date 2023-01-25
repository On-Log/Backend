package com.nanal.backend.domain.retrospect.dto;

import com.nanal.backend.domain.retrospect.entity.ExtraQuestion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExtraQuestionsDto {
    private String question;
    private String help;

    public static ExtraQuestionsDto makeExtraQuestionsDto(ExtraQuestion extraquestion){

        ExtraQuestionsDto extraQuestionsDto = new ExtraQuestionsDto();
        extraQuestionsDto.setQuestion(extraquestion.getContent());
        extraQuestionsDto.setHelp(extraquestion.getHelp());

        return extraQuestionsDto;
    }
}
