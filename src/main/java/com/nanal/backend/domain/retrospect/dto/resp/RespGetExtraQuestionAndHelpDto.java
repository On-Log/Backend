package com.nanal.backend.domain.retrospect.dto.resp;

import com.nanal.backend.domain.retrospect.dto.ExtraQuestionsDto;
import com.nanal.backend.domain.retrospect.entity.ExtraQuestion;
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
public class RespGetExtraQuestionAndHelpDto {
    private List<ExtraQuestionsDto> questionAndHelp;

    public static RespGetExtraQuestionAndHelpDto makeRespGetQuestionAndHelpDto(List<ExtraQuestion> questions){
        List<ExtraQuestionsDto> questionAndHelp = new ArrayList<>();
        for(ExtraQuestion q : questions){
            ExtraQuestionsDto extraQuestionsDto = ExtraQuestionsDto.makeExtraQuestionsDto(q);

            questionAndHelp.add(extraQuestionsDto);
        }
        RespGetExtraQuestionAndHelpDto respGetExtraQuestionAndHelpDto = RespGetExtraQuestionAndHelpDto.builder()
                .questionAndHelp(questionAndHelp)
                .build();

        return respGetExtraQuestionAndHelpDto;
    }
}
