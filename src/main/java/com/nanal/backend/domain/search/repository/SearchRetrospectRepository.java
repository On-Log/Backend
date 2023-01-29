package com.nanal.backend.domain.search.repository;

import com.nanal.backend.domain.retrospect.entity.QRetrospect;
import com.nanal.backend.domain.retrospect.entity.QRetrospectContent;
import com.nanal.backend.domain.retrospect.entity.Retrospect;
import com.nanal.backend.domain.search.dto.ReqSearchDto;
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
public class SearchRetrospectRepository {

    private final JPAQueryFactory queryFactory;

    QRetrospect retrospect = QRetrospect.retrospect;
    QRetrospectContent retrospectContent = QRetrospectContent.retrospectContent;

    public List<Retrospect> searchRetrospect(ReqSearchDto reqSearchDto) {

        return queryFactory
                .selectDistinct(retrospect)
                .from(retrospect)
                .join(retrospect.retrospectContents, retrospectContent)
                .where(
                        betweenDate(reqSearchDto.getStartDate(), reqSearchDto.getEndDate())
                                .and(containWordInAnswer(reqSearchDto.getSearchWord()))
                )
                .orderBy(retrospect.writeDate.desc())
                .offset(reqSearchDto.getOffset())
                .limit(reqSearchDto.getLimit())
                .fetch();
    }

    private BooleanBuilder containWordInAnswer(String word) {
        if(hasText(word)) return new BooleanBuilder(retrospectContent.answer.contains(word));
        else return new BooleanBuilder();
    }

    private BooleanBuilder startDate(LocalDateTime startDate) {
        if(startDate != null) return new BooleanBuilder(retrospect.writeDate.goe(LocalDateTime.of(startDate.toLocalDate(), LocalTime.MIN)));
        else return new BooleanBuilder();
    }

    private BooleanBuilder endDate(LocalDateTime endDate) {
        if(endDate != null) return new BooleanBuilder(retrospect.writeDate.loe(LocalDateTime.of(endDate.toLocalDate(), LocalTime.MAX)));
        else return new BooleanBuilder();
    }

    private BooleanBuilder betweenDate(LocalDateTime startDate, LocalDateTime endDate) {
        return startDate(startDate).and(endDate(endDate));
    }
}
