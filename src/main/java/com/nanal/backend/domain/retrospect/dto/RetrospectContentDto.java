package com.nanal.backend.domain.retrospect.dto;

import com.nanal.backend.entity.RetrospectContent;
import lombok.Data;

@Data
public class RetrospectContentDto {
    String answer;
    String question;

    public static RetrospectContentDto makeRetrospectContentDto(RetrospectContent retrospectContent) {
        RetrospectContentDto retrospectContentDto = new RetrospectContentDto();
        retrospectContentDto.setQuestion(retrospectContent.getQuestion());
        retrospectContentDto.setAnswer(retrospectContent.getAnswer());

        return retrospectContentDto;
    }
}
