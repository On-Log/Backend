package com.nanal.backend.domain.retrospect.dto;

import com.nanal.backend.domain.retrospect.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class QuestionsDto {

    private String question;
    private String help;

    public static QuestionsDto makeQuestionsDto(Question question){

        QuestionsDto questionsDto = new QuestionsDto();
        questionsDto.setQuestion(question.getContent());
        questionsDto.setHelp(question.getHelp());

        return questionsDto;
    }
}
