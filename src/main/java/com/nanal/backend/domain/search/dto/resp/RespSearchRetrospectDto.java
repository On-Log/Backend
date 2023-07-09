package com.nanal.backend.domain.search.dto.resp;

import com.nanal.backend.domain.retrospect.entity.Retrospect;
import com.nanal.backend.domain.search.dto.RetrospectInfo;

import java.util.List;

public class RespSearchRetrospectDto {
    private RetrospectInfo retrospectInfo;

    public RespSearchRetrospectDto(String searchWord,
                                   List<Retrospect> retrospectList,
                                   Integer nextRetrospectCount) {
        this.retrospectInfo = new RetrospectInfo(retrospectList, nextRetrospectCount, searchWord);
    }
}
