package com.nanal.backend.domain.search.service;

import com.nanal.backend.domain.diary.repository.DiaryRepository;
import com.nanal.backend.domain.retrospect.repository.RetrospectRepository;
import com.nanal.backend.domain.search.dto.ReqSearchDto;
import com.nanal.backend.domain.search.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SearchService {

    private final SearchRepository searchRepository;

    public void search(ReqSearchDto reqSearchDto) {

        searchRepository.searchDiary(reqSearchDto);

    }
}
