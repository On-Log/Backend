package com.nanal.backend.domain.retrospect.dto.resp;

import com.nanal.backend.domain.retrospect.entity.ExtraQuestion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RespGetExtraQuestionAndHelpDto {
    private List<ExtraQuestionsDto> questionAndHelp;

    public static RespGetExtraQuestionAndHelpDto createRespGetQuestionAndHelpDto(List<ExtraQuestion> questions){
        List<ExtraQuestionsDto> questionAndHelp = questions.stream()
                .map(ExtraQuestionsDto::makeExtraQuestionsDto)
                .collect(Collectors.toList());

        RespGetExtraQuestionAndHelpDto respGetExtraQuestionAndHelpDto = RespGetExtraQuestionAndHelpDto.builder()
                .questionAndHelp(questionAndHelp)
                .build();

        return respGetExtraQuestionAndHelpDto;
    }
}
