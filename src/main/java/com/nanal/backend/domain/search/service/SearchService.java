package com.nanal.backend.domain.search.service;

import com.nanal.backend.domain.diary.entity.Diary;
import com.nanal.backend.domain.search.dto.ReqSearchDto;
import com.nanal.backend.domain.search.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class SearchService {

    private final SearchRepository searchRepository;

    public List<Diary> search(ReqSearchDto reqSearchDto) {

        List<Diary> diaryList = searchRepository.searchDiary(reqSearchDto);
        for (Diary d : diaryList) {
            System.out.println(d.getWriteDate());
        }
        return diaryList;

    }
}
