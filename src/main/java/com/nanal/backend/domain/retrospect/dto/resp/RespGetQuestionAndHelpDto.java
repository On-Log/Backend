package com.nanal.backend.domain.retrospect.dto.resp;

import com.nanal.backend.domain.retrospect.entity.Question;
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
public class RespGetQuestionAndHelpDto {
    private List<QuestionsDto> questionAndHelp;

    public static RespGetQuestionAndHelpDto createRespGetQuestionAndHelpDto(List<Question> questions){
        List<QuestionsDto> questionAndHelp = questions.stream()
                .map(QuestionsDto::makeQuestionsDto)
                .collect(Collectors.toList());

        RespGetQuestionAndHelpDto respGetQuestionAndHelpDto = RespGetQuestionAndHelpDto.builder()
                .questionAndHelp(questionAndHelp)
                .build();

        return respGetQuestionAndHelpDto;
    }
}
