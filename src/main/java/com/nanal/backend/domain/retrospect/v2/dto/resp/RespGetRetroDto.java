package com.nanal.backend.domain.retrospect.v2.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nanal.backend.domain.retrospect.dto.resp.RetrospectContentDto;
import com.nanal.backend.domain.retrospect.dto.resp.RetrospectKeywordDto;
import com.nanal.backend.domain.retrospect.entity.Retrospect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RespGetRetroDto {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime writeDate;

    private List<RetrospectContentDto> contents;

    private List<RetrospectKeywordDto> keywords;

    private Integer week;

    public static RespGetRetroDto createRespGetRetroDto(Retrospect selectRetrospect, Integer week) {
        List<RetrospectKeywordDto> retrospectKeywordList = selectRetrospect.getRetrospectKeywords().stream()
                .map(retrospectKeyword -> new RetrospectKeywordDto(retrospectKeyword))
                .collect(Collectors.toList());

        List<RetrospectContentDto> retrospectContentList = selectRetrospect.getRetrospectContents().stream()
                .map(retrospectContent -> new RetrospectContentDto(retrospectContent))
                .collect(Collectors.toList());

        RespGetRetroDto respGetSearchRetroDto = RespGetRetroDto.builder()
                .writeDate(selectRetrospect.getWriteDate())
                .contents(retrospectContentList)
                .keywords(retrospectKeywordList)
                .week(week)
                .build();
        return respGetSearchRetroDto;
    }
}
