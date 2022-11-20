package com.nanal.backend.domain.retrospect.dto;

import com.nanal.backend.entity.RetrospectKeyword;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class RetrospectKeywordDto {
    @Schema(description = "키워드" , example = "공부")
    String keyword;

    @Schema(description = "분류" , example = "행복한 기억")
    String classify;

    public static RetrospectKeywordDto makeRetrospectKeywordDto(RetrospectKeyword retrospectKeyword) {
        RetrospectKeywordDto retrospectKeywordDto = new RetrospectKeywordDto();
        retrospectKeywordDto.setKeyword(retrospectKeyword.getKeyword());
        retrospectKeywordDto.setClassify(retrospectKeyword.getClassify());

        return retrospectKeywordDto;
    }
}
