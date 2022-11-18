package com.nanal.backend.domain.retrospect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RespGetQuestionAndHelpDto {

    private List<String> questionAndHelp;

}
