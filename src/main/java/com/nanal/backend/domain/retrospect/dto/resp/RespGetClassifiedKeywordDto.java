package com.nanal.backend.domain.retrospect.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RespGetClassifiedKeywordDto {
    private List<ClassifyDto> classify;
    private String val;

    private static List<String> keyWordClass = new ArrayList<>(Arrays.asList("그때 그대로 의미있었던 행복한 기억", "나를 힘들게 했지만 도움이 된 기억", "놓아줘도 괜찮은 기억"));
    public static RespGetClassifiedKeywordDto makeRespGetExistRetrospectKeyword(List<ClassifyDto> classifyDtos, String val){
    RespGetClassifiedKeywordDto respGetClassifiedKeywordDto = RespGetClassifiedKeywordDto.builder()
            .val(val)
            .classify(classifyDtos)
            .build();

    return respGetClassifiedKeywordDto;

    }
}
