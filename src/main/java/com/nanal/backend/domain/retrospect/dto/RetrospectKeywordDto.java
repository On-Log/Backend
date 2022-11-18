package com.nanal.backend.domain.retrospect.dto;

import com.nanal.backend.entity.RetrospectKeyword;
import lombok.Data;


@Data
public class RetrospectKeywordDto {
    String keyword;
    String classify;

    public static RetrospectKeywordDto makeRetrospectKeywordDto(RetrospectKeyword retrospectKeyword) {
        RetrospectKeywordDto retrospectKeywordDto = new RetrospectKeywordDto();
        retrospectKeywordDto.setKeyword(retrospectKeyword.getKeyword());
        retrospectKeywordDto.setClassify(retrospectKeyword.getClassify());

        return retrospectKeywordDto;
    }
}
