package com.nanal.backend.domain.search.service;

import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.domain.auth.repository.MemberRepository;
import com.nanal.backend.domain.diary.entity.Diary;
import com.nanal.backend.domain.retrospect.entity.Retrospect;
import com.nanal.backend.domain.search.dto.ReqSearchDto;
import com.nanal.backend.domain.search.dto.RespSearchDto;
import com.nanal.backend.domain.search.repository.SearchDiaryRepository;
import com.nanal.backend.domain.search.repository.SearchRetrospectRepository;
import com.nanal.backend.global.exception.customexception.MemberAuthException;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Timed("search.api")
@Transactional
@RequiredArgsConstructor
@Service
public class SearchService {

    private final SearchDiaryRepository searchDiaryRepository;
    private final SearchRetrospectRepository searchRetrospectRepository;

    public RespSearchDto search(ReqSearchDto reqSearchDto) {

        // 일기 검색
        List<Diary> diaryList = searchDiaryRepository.searchDiary(reqSearchDto);

        // 회고 검색
        List<Retrospect> retrospectList = searchRetrospectRepository.searchRetrospect(reqSearchDto);

        return new RespSearchDto(diaryList, retrospectList);
    }
}
