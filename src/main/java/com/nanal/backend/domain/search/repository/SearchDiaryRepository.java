package com.nanal.backend.domain.search.repository;

import com.nanal.backend.domain.diary.entity.Diary;
import com.nanal.backend.domain.diary.entity.QDiary;
import com.nanal.backend.domain.diary.entity.QKeyword;
import com.nanal.backend.domain.search.dto.req.ReqSearchDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Repository
public class SearchDiaryRepository {

    QDiary diary = QDiary.diary;
    QKeyword keyword = QKeyword.keyword;

    private final JPAQueryFactory queryFactory;

    public List<Diary> searchDiary(ReqSearchDto reqSearchDto, Long memberId) {

        return queryFactory
                .selectDistinct(diary)
                .from(diary)
                .join(diary.keywords, keyword)
                .where(
                        betweenDate(reqSearchDto.getStartDate(), reqSearchDto.getEndDate())
                                .and(containWord(reqSearchDto.getSearchWord())
                                        .and(isEqualMember(memberId)))
                )
                .orderBy(diary.writeDate.desc())
                .offset(reqSearchDto.getOffset())
                .limit(reqSearchDto.getLimit())
                .fetch();
    }

    public Integer countLeftDiary(ReqSearchDto reqSearchDto, Long memberId) {
        return queryFactory
                .selectDistinct(diary.diaryId)
                .from(diary)
                .join(diary.keywords, keyword)
                .where(
                        betweenDate(reqSearchDto.getStartDate(), reqSearchDto.getEndDate())
                                .and(containWord(reqSearchDto.getSearchWord())
                                        .and(isEqualMember(memberId)))
                )
                .orderBy(diary.writeDate.desc())
                .offset(reqSearchDto.getOffset() + reqSearchDto.getLimit())
                .limit(reqSearchDto.getLimit())
                .fetch()
                .size();
    }

    private BooleanBuilder isEqualMember(Long memberId) {
        if(memberId != null) return new BooleanBuilder(diary.member.memberId.eq(memberId));
        else return new BooleanBuilder();
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

    private BooleanBuilder fromDate(LocalDateTime fromDate) {
        if(fromDate != null) {
            fromDate = fromDate.toLocalDate().atStartOfDay();
            return new BooleanBuilder(diary.writeDate.goe(fromDate));
        }
        else return new BooleanBuilder();
    }

    private BooleanBuilder toDate(LocalDateTime toDate) {
        if(toDate != null) {
            toDate = toDate.toLocalDate().atTime(LocalTime.MAX).withNano(0);
            return new BooleanBuilder(diary.writeDate.loe(toDate));
        }
        else return new BooleanBuilder();
    }

    private BooleanBuilder betweenDate(LocalDateTime fromDate, LocalDateTime toDate) {
        return fromDate(fromDate).and(toDate(toDate));
    }
}
