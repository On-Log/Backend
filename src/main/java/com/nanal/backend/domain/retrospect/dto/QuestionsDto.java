package com.nanal.backend.domain.retrospect.dto;

import com.nanal.backend.domain.retrospect.entity.Question;
import lombok.Data;

@Data
public class QuestionsDto {

    private String quetsion;
    private String help;

    public static QuestionsDto makeQuestionsDto(Question question){

        QuestionsDto questionsDto = new QuestionsDto();
        questionsDto.setQuetsion(question.getContent());
        questionsDto.setHelp(question.getHelp());

        return questionsDto;
    }
}
