package com.nanal.backend.domain.retrospect.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nanal.backend.domain.retrospect.entity.Retrospect;
import com.nanal.backend.domain.retrospect.entity.RetrospectContent;
import com.nanal.backend.domain.retrospect.entity.RetrospectKeyword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RespGetRetroDto {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime writeDate;

    private List<RetrospectContentDto> contents;

    private List<RetrospectKeywordDto> keywords;

    public static RespGetRetroDto makeRespGetRetroDto(Retrospect selectRetrospect) {
        List<RetrospectKeywordDto> keywords = new ArrayList<>();
        for (RetrospectKeyword retrospectKeyword : selectRetrospect.getRetrospectKeywords()) {
            RetrospectKeywordDto retrospectKeywordDto = RetrospectKeywordDto.makeRetrospectKeywordDto(retrospectKeyword);
            keywords.add(retrospectKeywordDto);
        }

        List<RetrospectContentDto> contents = new ArrayList<>();
        for (RetrospectContent retrospectContent : selectRetrospect.getRetrospectContents()) {
            RetrospectContentDto retrospectContentDto = RetrospectContentDto.makeRetrospectContentDto(retrospectContent);
            contents.add(retrospectContentDto);
        }

        RespGetRetroDto respGetRetroDto = RespGetRetroDto.builder()
                .writeDate(selectRetrospect.getWriteDate())
                .contents(contents)
                .keywords(keywords)
                .build();
        return respGetRetroDto;
    }
}
