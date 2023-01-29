package com.nanal.backend.domain.search.service;

import com.nanal.backend.domain.diary.entity.Diary;
import com.nanal.backend.domain.retrospect.entity.Retrospect;
import com.nanal.backend.domain.search.dto.ReqSearchDto;
import com.nanal.backend.domain.search.dto.RespSearchDto;
import com.nanal.backend.domain.search.repository.SearchDiaryRepository;
import com.nanal.backend.domain.search.repository.SearchRetrospectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class SearchService {

    private final SearchDiaryRepository searchDiaryRepository;
    private final SearchRetrospectRepository searchRetrospectRepository;

    public RespSearchDto search(ReqSearchDto reqSearchDto) {

        // 일기 검색
        List<Diary> diaryList = searchDiaryRepository.searchDiary(reqSearchDto);

        /*for (Diary d : diaryList) {
            System.out.println(d.getWriteDate());
            d.getKeywords().stream().forEach((o) -> System.out.println(o.getWord()));
        }*/

        // 회고 검색
        List<Retrospect> retrospectList = searchRetrospectRepository.searchRetrospect(reqSearchDto);
        /*for (Retrospect r : retrospectList) {
            System.out.println(r.getWriteDate());
            r.getRetrospectKeywords().stream().forEach((o) -> System.out.println(o.getKeyword()));
        }*/

        return new RespSearchDto(diaryList, retrospectList);
    }
}
