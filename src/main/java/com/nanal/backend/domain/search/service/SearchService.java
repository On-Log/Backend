package com.nanal.backend.domain.search.service;

import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.domain.auth.repository.MemberRepository;
import com.nanal.backend.domain.diary.entity.Diary;
import com.nanal.backend.domain.retrospect.entity.Retrospect;
import com.nanal.backend.domain.search.dto.req.ReqSearchDto;
import com.nanal.backend.domain.search.dto.resp.RespSearchDto;
import com.nanal.backend.domain.search.repository.SearchDiaryRepository;
import com.nanal.backend.domain.search.repository.SearchRetrospectRepository;
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

    private final MemberRepository memberRepository;
    private final SearchDiaryRepository searchDiaryRepository;
    private final SearchRetrospectRepository searchRetrospectRepository;

    public RespSearchDto search(String socialId, ReqSearchDto reqSearchDto) {
        Member member = memberRepository.findMember(socialId);

        // 일기 검색
        List<Diary> diaryList = searchDiaryRepository.searchDiary(reqSearchDto, member.getMemberId());
        Integer nextDiaryCount = searchDiaryRepository.countLeftDiary(reqSearchDto, member.getMemberId());

        // 회고 검색
        List<Retrospect> retrospectList = searchRetrospectRepository.searchRetrospect(reqSearchDto, member.getMemberId());
        Integer nextRetrospectCount = searchRetrospectRepository.countLeftRetrospect(reqSearchDto, member.getMemberId());

        return new RespSearchDto(reqSearchDto.getSearchWord(), diaryList, retrospectList, nextDiaryCount, nextRetrospectCount);
    }
}
