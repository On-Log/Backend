package com.nanal.backend.domain.diary.service;

import com.nanal.backend.domain.diary.dto.req.*;
import com.nanal.backend.domain.diary.dto.resp.RespGetCalendarDto;
import com.nanal.backend.domain.diary.dto.resp.RespGetDiaryDto;
import com.nanal.backend.domain.diary.dto.resp.RespGetEmotionDto;
import com.nanal.backend.domain.diary.entity.Diary;
import com.nanal.backend.domain.diary.entity.Emotion;
import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.domain.diary.exception.DiaryAlreadyExistException;
import com.nanal.backend.domain.diary.exception.DiaryNotFoundException;
import com.nanal.backend.global.exception.customexception.MemberAuthException;
import com.nanal.backend.domain.diary.repository.DiaryRepository;
import com.nanal.backend.domain.diary.repository.EmotionRepository;
import com.nanal.backend.domain.auth.repository.MemberRepository;
import com.nanal.backend.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class DiaryService {

    private final MemberRepository memberRepository;
    private final DiaryRepository diaryRepository;
    private final EmotionRepository emotionRepository;

    public RespGetCalendarDto getCalendar(String socialId, ReqGetCalendarDto reqGetCalendarDto) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> MemberAuthException.EXCEPTION);

        // 회고일 체크
        LocalDateTime now = LocalDateTime.now();
        Boolean isRetrospectDay = member.isRetrospectDay(now);

        // 요청된 기간내 유저의 기록이 존재하는 날 조회
        List<LocalDateTime> existDiaryDate = getExistDiaryDateList(member.getMemberId(), reqGetCalendarDto.getSelectDate());

        // 회고 요일과 현재 날짜로 일기 작성 가능주 구하기

        LocalDateTime nextDayOfPrevRetroDate = getNextDayOfPrevRetroDate(member.getRetrospectDay(), now);
        LocalDateTime retroDate = getRetroDate(member.getRetrospectDay(), now);

        return RespGetCalendarDto.builder()
                .isRetrospectDay(isRetrospectDay)
                .existDiaryDate(existDiaryDate)
                .nextDayOfPrevRetroDate(nextDayOfPrevRetroDate)
                .retroDate(retroDate)
                .build();
    }

    public void saveDiary(String socialId, ReqSaveDiaryDto reqSaveDiaryDto) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> MemberAuthException.EXCEPTION);

        // 해당 날짜에 작성한 일기 존재하는지 체크
        checkDiaryAlreadyExist(member.getMemberId(), reqSaveDiaryDto.getDate());

        // 일기 Entity 생성
        Diary diary = Diary.createDiary(member, reqSaveDiaryDto);

        // 일기 저장
        diaryRepository.save(diary);
    }

    public RespGetDiaryDto getDiary(String socialId, ReqGetDiaryDto reqGetDiaryDto) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> MemberAuthException.EXCEPTION);

        // 조회할 일기 가져오기
        Diary selectDiary = getDiary(member.getMemberId(), reqGetDiaryDto.getDate());

        // 조회한 일기로 반환값 생성
        return RespGetDiaryDto.createRespGetDiaryDto(selectDiary);
    }

    public void updateDiary(String socialId, ReqEditDiaryDto reqEditDiary) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> MemberAuthException.EXCEPTION);

        // 수정할 일기 조회
        Diary updateDiary = getDiary(member.getMemberId(), reqEditDiary.getDate());

        // 일기 수정
        updateDiary.updateDiary(reqEditDiary);
    }

    public void deleteDiary(String socialId, ReqDeleteDiaryDto reqDeleteDiaryDto) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> MemberAuthException.EXCEPTION);

        // 삭제할 일기 가져오기
        Diary deleteDiary = getDiary(member.getMemberId(), reqDeleteDiaryDto.getDate());
        // 기존 일기 삭제
        diaryRepository.delete(deleteDiary);
    }

    public RespGetEmotionDto getEmotion() {
        // 감정어 조회
        RespGetEmotionDto respGetEmotionDto = getRespGetEmotionDto(emotionRepository.findAll());

        return respGetEmotionDto;
    }


    //===편의 메서드===//

    private Diary getDiary(Long memberId, LocalDateTime date) {
        LocalDate tempDate = date.toLocalDate();
        LocalDateTime startDate = tempDate.atStartOfDay();
        LocalDateTime endDate = tempDate.atTime(LocalTime.MAX);

        // 선택한 날에 작성한 일기 조회
        Diary findDiary = diaryRepository.findDiaryByMemberAndWriteDate(memberId, startDate, endDate)
                .orElseThrow(() -> DiaryNotFoundException.EXCEPTION);

        return findDiary;
    }

    private void checkDiaryAlreadyExist(Long memberId, LocalDateTime date) {
        LocalDate tempDate = date.toLocalDate();
        LocalDateTime startDate = tempDate.atStartOfDay();
        LocalDateTime endDate = tempDate.atTime(LocalTime.MAX);

        List<Diary> findDiaryList = diaryRepository.findDiaryListByMemberAndWriteDate(memberId, startDate, endDate);

        if (findDiaryList.size() != 0) throw DiaryAlreadyExistException.EXCEPTION;
    }

    private List<LocalDateTime> getExistDiaryDateList(Long memberId, LocalDateTime date) {
        LocalDate tempDate = date.toLocalDate();
        LocalDateTime startDate = tempDate.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endDate = tempDate.withDayOfMonth(tempDate.lengthOfMonth()).atTime(LocalTime.MAX);

        // 선택한 날에 작성한 일기리스트 조회
        List<Diary> writeDates = diaryRepository.findDiaryListByMemberAndWriteDate(memberId, startDate, endDate);

        // 일기리스트의 작성날짜 List 생성
        return writeDates.stream()
                .map(Diary::getWriteDate)
                .collect(Collectors.toList());
    }

    public LocalDateTime getRetroDate(DayOfWeek retrospectDay, LocalDateTime now) {
        // 다음 회고일
        return now.with(TemporalAdjusters.nextOrSame(retrospectDay));
    }

    private LocalDateTime getNextDayOfPrevRetroDate(DayOfWeek retrospectDay, LocalDateTime now) {
        // 이전 회고일
        LocalDateTime prevRetroDate = now.with(TemporalAdjusters.previous(retrospectDay));

        // 해당 주는 이전 회고일 다음날부터 다음 회고 일까지이므로 '이전 회고일 + 1' 을 해줘야함
        return prevRetroDate.plusDays(1);
    }

    private RespGetEmotionDto getRespGetEmotionDto(List<Emotion> emotions) {
        List<String> emotionWords = emotions.stream()
                .map(Emotion::getEmotion)
                .collect(Collectors.toList());

        return new RespGetEmotionDto(emotionWords);
    }
}
