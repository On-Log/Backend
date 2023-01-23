package com.nanal.backend.domain.search.service;

import com.nanal.backend.domain.diary.repository.DiaryRepository;
import com.nanal.backend.domain.retrospect.repository.RetrospectRepository;
import com.nanal.backend.domain.search.dto.ReqSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SearchService {

    private final DiaryRepository diaryRepository;
    private final RetrospectRepository retrospectRepository;

    public void search(ReqSearchDto reqSearchDto) {



    }
}
