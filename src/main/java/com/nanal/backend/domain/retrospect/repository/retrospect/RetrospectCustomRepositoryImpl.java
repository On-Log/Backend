package com.nanal.backend.domain.retrospect.repository.retrospect;

import com.nanal.backend.domain.auth.entity.QMember;
import com.nanal.backend.domain.diary.dto.resp.RetrospectInfoDto;
import com.nanal.backend.domain.diary.exception.RetrospectAlreadyWrittenException;
import com.nanal.backend.domain.retrospect.entity.QRetrospect;
import com.nanal.backend.domain.retrospect.entity.Retrospect;
import com.nanal.backend.domain.retrospect.exception.RetrospectAllDoneException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RetrospectCustomRepositoryImpl implements RetrospectCustomRepository{

    private final JPAQueryFactory queryFactory;

    QRetrospect retrospect = QRetrospect.retrospect;

    @Override
    public void checkRetrospectAlreadyExist(Long memberId, LocalDateTime date) {
        Optional<Retrospect> findRetrospect = findRetrospectByMemberAndWriteDate(memberId, date, date);

        if(findRetrospect.isPresent()) throw RetrospectAlreadyWrittenException.EXCEPTION;
    }

    @Override
    public Boolean existDiaryDate(Long memberId, LocalDateTime date) {
        Optional<Retrospect> findRetrospect = findRetrospectByMemberAndWriteDate(memberId, date, date);

        if(findRetrospect.isPresent()) return true;
        else return false;
    }

    @Override
    public Boolean checkRetroNotOverFive(Long memberId, LocalDateTime fromDate, LocalDateTime toDate) {
        List<Retrospect> retrospects = findRetrospectListByMemberAndWriteDate(memberId, fromDate, toDate);

        if(retrospects.size() >= 5) return false;
        else return true;
    }

    @Override
    public void checkRetroCount(Long memberId, LocalDateTime fromDate, LocalDateTime toDate) {
        List<Retrospect> retrospects = findRetrospectListByMemberAndWriteDate(memberId, fromDate, toDate);

        if(retrospects.size() >= 5) throw RetrospectAllDoneException.EXCEPTION;
    }


    @Override
    public List<String> getRetrospectGoal(Long memberId, LocalDateTime fromDate, LocalDateTime toDate) {
        List<Retrospect> goals = findRetrospectListByMemberAndWriteDate(memberId, fromDate, toDate);
        return goals.stream()
                .map(Retrospect::getGoal)
                .collect(Collectors.toList());
    }

    @Override
    public List<RetrospectInfoDto> findRetrospectList(Long memberId, LocalDateTime fromDate, LocalDateTime toDate) {
        List<Retrospect> retrospectList = findRetrospectListByMemberAndWriteDate(memberId, fromDate, toDate);

        return retrospectList.stream()
                .map(RetrospectInfoDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Retrospect> findRetrospectByMemberAndWriteDate(Long memberId, LocalDateTime fromDate, LocalDateTime toDate) {
        return queryFactory
                .selectFrom(retrospect)
                .where(isEqualMember(memberId)
                        .and(betweenDate(fromDate, toDate)))
                .stream().findAny();
    }

    @Override
    public List<Retrospect> findRetrospectListByMemberAndWriteDate(Long memberId, LocalDateTime fromDate, LocalDateTime toDate) {
        return queryFactory
                .selectFrom(retrospect)
                .where(isEqualMember(memberId)
                        .and(betweenDate(fromDate, toDate)))
                .orderBy(retrospect.writeDate.asc())
                .fetch();
    }

    private BooleanBuilder isEqualMember(Long memberId) {
        if(memberId != null) return new BooleanBuilder(retrospect.member.memberId.eq(memberId));
        else return new BooleanBuilder();
    }

    private BooleanBuilder fromDate(LocalDateTime fromDate) {
        if(fromDate != null) {
            fromDate = fromDate.toLocalDate().atStartOfDay();
            return new BooleanBuilder(retrospect.writeDate.goe(fromDate));
        }
        else return new BooleanBuilder();
    }

    private BooleanBuilder toDate(LocalDateTime toDate) {
        if(toDate != null) {
            toDate = toDate.toLocalDate().atTime(LocalTime.MAX).withNano(0);
            return new BooleanBuilder(retrospect.writeDate.loe(toDate));
        }
        else return new BooleanBuilder();
    }

    private BooleanBuilder betweenDate(LocalDateTime fromDate, LocalDateTime toDate) {
        return fromDate(fromDate).and(toDate(toDate));
    }
}
