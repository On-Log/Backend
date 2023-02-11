package com.nanal.backend.domain.retrospect.dto.resp;

import com.nanal.backend.domain.retrospect.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RespGetQuestionAndHelpDto {
    private List<QuestionsDto> questionAndHelp;

    public static RespGetQuestionAndHelpDto makeRespGetQuestionAndHelpDto(List<Question> questions){
        List<QuestionsDto> questionAndHelp = new ArrayList<>();
        for(Question q : questions){
            QuestionsDto questionsDto = QuestionsDto.makeQuestionsDto(q);

            questionAndHelp.add(questionsDto);
        }
        RespGetQuestionAndHelpDto respGetQuestionAndHelpDto = RespGetQuestionAndHelpDto.builder()
                .questionAndHelp(questionAndHelp)
                .build();

        return respGetQuestionAndHelpDto;
    }
}
