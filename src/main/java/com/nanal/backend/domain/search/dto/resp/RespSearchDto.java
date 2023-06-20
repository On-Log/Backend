package com.nanal.backend.domain.search.dto.resp;

import com.nanal.backend.domain.diary.entity.Diary;
import com.nanal.backend.domain.retrospect.entity.Retrospect;
import com.nanal.backend.domain.search.dto.DiaryInfo;
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
public class RespSearchDto {

    private DiaryInfo diaryInfo;

    private RetrospectInfo retrospectInfo;

    public RespSearchDto(String searchWord,
                         List<Diary> diaryList,
                         List<Retrospect> retrospectList,
                         Integer nextDiaryCount,
                         Integer nextRetrospectCount) {
        this.diaryInfo = new DiaryInfo(diaryList, nextDiaryCount);
        this.retrospectInfo = new RetrospectInfo(retrospectList, nextRetrospectCount, searchWord);
    }
}
