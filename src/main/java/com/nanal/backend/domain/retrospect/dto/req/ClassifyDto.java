package com.nanal.backend.domain.retrospect.dto.req;

import com.nanal.backend.domain.retrospect.entity.RetrospectKeyword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClassifyDto {
    private List<ClassifyKeywordDto> weeklykeywords;

    public static ClassifyDto makeClassifyDto(List<RetrospectKeyword> classifiedKeyword) {
        List<ClassifyKeywordDto> weeklykeywords = new ArrayList<>();
        for (RetrospectKeyword r : classifiedKeyword) {
            ClassifyKeywordDto classifyKeywordDto = new ClassifyKeywordDto(r.getKeyword());

            weeklykeywords.add(classifyKeywordDto);
        }
        ClassifyDto classifyDto = ClassifyDto.builder()
                .weeklykeywords(weeklykeywords)
                .build();

        return classifyDto;

    }
}
