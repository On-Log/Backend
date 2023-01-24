package com.nanal.backend.domain.retrospect.dto;

import com.nanal.backend.domain.retrospect.entity.RetrospectKeyword;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RetrospectKeywordDto {
    @NotBlank(message = "classify 는 비어있을 수 없습니다.")
    @Size(max = 20, message="classify 는 최대 20개의 문자만 입력 가능합니다.")
    String classify;

    @NotBlank(message = "keyword 는 비어있을 수 없습니다.")
    @Size(max = 5, message="keyword 는 최대 5개의 문자만 입력 가능합니다.")
    String keyword;


    public static RetrospectKeywordDto makeRetrospectKeywordDto(RetrospectKeyword retrospectKeyword) {
        RetrospectKeywordDto retrospectKeywordDto = new RetrospectKeywordDto();
        retrospectKeywordDto.setKeyword(retrospectKeyword.getKeyword());
        retrospectKeywordDto.setClassify(retrospectKeyword.getClassify());

        return retrospectKeywordDto;
    }
}
