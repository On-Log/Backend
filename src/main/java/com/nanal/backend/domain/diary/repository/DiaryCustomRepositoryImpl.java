package com.nanal.backend.domain.diary.repository;

import com.nanal.backend.domain.auth.entity.QMember;
import com.nanal.backend.domain.diary.entity.Diary;
import com.nanal.backend.domain.diary.entity.QDiary;
import com.nanal.backend.domain.diary.exception.DiaryAlreadyExistException;
import com.nanal.backend.domain.diary.exception.DiaryNotFoundException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DiaryCustomRepositoryImpl implements DiaryCustomRepository{

    private final JPAQueryFactory queryFactory;

    QDiary diary = QDiary.diary;
    QMember member = QMember.member;

    @Override
    public List<LocalDateTime> findExistDiaryDateList(Long memberId, LocalDateTime fromDate, LocalDateTime toDate) {
        // 선택한 날에 작성한 일기리스트 조회
        List<Diary> writeDates = findDiaryListByMemberAndWriteDate(memberId, fromDate, toDate);

        // 일기리스트의 작성날짜 List 생성
        return writeDates.stream()
                .map(Diary::getWriteDate)
                .collect(Collectors.toList());
    }

    @Override
    public void checkTodayDiaryAlreadyExist(Long memberId, LocalDateTime today) {
        List<Diary> findDiaryList = findDiaryListByMemberAndWriteDate(memberId, today, today);

        if (findDiaryList.size() != 0) throw DiaryAlreadyExistException.EXCEPTION;
    }

    @Override
    public Diary findDiary(Long memberId, LocalDateTime date) {
        return findDiaryByMemberAndWriteDate(memberId, date, date)
                .orElseThrow(() -> DiaryNotFoundException.EXCEPTION);
    }

    @Override
    public List<Diary> findDiaryListByMemberAndWriteDate(Long memberId, LocalDateTime fromDate, LocalDateTime toDate) {
        return queryFactory
                .selectFrom(diary)
                .where(isEqualMember(memberId)
                        .and(betweenDate(fromDate, toDate)))
                .fetch();
    }

    @Override
    public Optional<Diary> findDiaryByMemberAndWriteDate(Long memberId, LocalDateTime fromDate, LocalDateTime toDate) {

        return queryFactory
                .selectFrom(diary)
                .where(isEqualMember(memberId)
                        .and(betweenDate(fromDate, toDate)))
                .stream().findAny();
    }

    private BooleanBuilder isEqualMember(Long memberId) {
        if(memberId != null) return new BooleanBuilder(member.memberId.eq(memberId));
        else return new BooleanBuilder();
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
