package com.nanal.backend.domain.search.dto.resp;

import com.nanal.backend.domain.retrospect.entity.Retrospect;
import com.nanal.backend.domain.search.dto.RetrospectInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RespSearchRetrospectDto {
    private RetrospectInfo retrospectInfo;

    public RespSearchRetrospectDto(String searchWord,
                                   List<Retrospect> retrospectList,
                                   Integer nextRetrospectCount) {
        this.retrospectInfo = new RetrospectInfo(retrospectList, nextRetrospectCount, searchWord);
    }
}
