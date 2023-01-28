package com.nanal.backend.domain.search.repository;

import com.nanal.backend.domain.diary.entity.QDiary;
import com.nanal.backend.domain.diary.entity.QKeyword;
import com.nanal.backend.domain.search.dto.ReqSearchDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Repository
public class SearchRepository {

    // qclass iv로 선언시 스레드 세이프한지 체크
    QDiary diary = QDiary.diary;
    QKeyword keyword = QKeyword.keyword;

    private final JPAQueryFactory queryFactory;

    public List<?> searchDiary(ReqSearchDto reqSearchDto) {

        queryFactory
                .selectDistinct(diary)
                .from(diary)
                .join(diary.keywords, keyword).fetchJoin()
                .where(
                        containWord(reqSearchDto.getSearchWord())
                                .and(betweenDate(reqSearchDto.getStartDate(), reqSearchDto.getEndDate()))
                )
                .orderBy(diary.writeDate.desc())
                .offset(reqSearchDto.getOffset())
                .limit(reqSearchDto.getLimit())
                .fetch();
        return new ArrayList<>();
    }

    private BooleanBuilder containWordInContent(String word) {
        if(hasText(word)) return new BooleanBuilder(diary.content.contains(word));
        else return new BooleanBuilder();
    }

    private BooleanBuilder containWordInKeyword(String word) {
        if(hasText(word)) return new BooleanBuilder(keyword.word.contains(word));
        else return new BooleanBuilder();
    }

    private BooleanBuilder containWord(String word) {
        return containWordInContent(word).or(containWordInKeyword(word));
    }

    private BooleanBuilder startDate(LocalDateTime startDate) {
        if(startDate != null) return new BooleanBuilder(diary.writeDate.goe(LocalDateTime.of(startDate.toLocalDate(), LocalTime.MIN)));
        else return new BooleanBuilder();
    }

    private BooleanBuilder endDate(LocalDateTime endDate) {
        if(endDate != null) return new BooleanBuilder(diary.writeDate.loe(LocalDateTime.of(endDate.toLocalDate(), LocalTime.MAX)));
        else return new BooleanBuilder();
    }

    private BooleanBuilder betweenDate(LocalDateTime startDate, LocalDateTime endDate) {
        return startDate(startDate).and(endDate(endDate));
    }


}
