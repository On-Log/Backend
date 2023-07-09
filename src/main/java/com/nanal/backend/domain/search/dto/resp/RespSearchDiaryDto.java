package com.nanal.backend.domain.search.dto.resp;

import com.nanal.backend.domain.diary.entity.Diary;
import com.nanal.backend.domain.search.dto.DiaryInfo;

import java.util.List;

public class RespSearchDiaryDto {
    private DiaryInfo diaryInfo;

    public RespSearchDiaryDto(String searchWord,
                              List<Diary> diaryList,
                              Integer nextDiaryCount) {
        this.diaryInfo = new DiaryInfo(diaryList, nextDiaryCount, searchWord);
    }
}
