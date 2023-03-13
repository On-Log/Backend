package com.nanal.backend.domain.diary.service;

import com.nanal.backend.domain.diary.domain.DiaryWritableWeek;
import com.nanal.backend.domain.diary.dto.req.*;
import com.nanal.backend.domain.diary.dto.resp.RespGetCalendarDto;
import com.nanal.backend.domain.diary.dto.resp.RespGetDiaryDto;
import com.nanal.backend.domain.diary.dto.resp.RespGetEmotionDto;
import com.nanal.backend.domain.diary.dto.resp.RetrospectInfoDto;
import com.nanal.backend.domain.diary.entity.Diary;
import com.nanal.backend.domain.diary.entity.Emotion;
import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.domain.diary.exception.*;
import com.nanal.backend.domain.retrospect.repository.retrospect.RetrospectRepository;
import com.nanal.backend.domain.diary.repository.diary.DiaryRepository;
import com.nanal.backend.domain.diary.repository.EmotionRepository;
import com.nanal.backend.domain.auth.repository.MemberRepository;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Timed("diary.api")
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class DiaryService {

    private final MemberRepository memberRepository;
    private final DiaryRepository diaryRepository;
    private final EmotionRepository emotionRepository;
    private final RetrospectRepository retrospectRepository;

    @Counted("diary.api.count")
    @Transactional(readOnly = true)
    public RespGetCalendarDto getCalendar(String socialId, ReqGetCalendarDto reqGetCalendarDto) {
        // socialId 로 유저 조회
        Member member = memberRepository.findMember(socialId);

        RespGetCalendarDto calendar = getRespGetCalendarDto(reqGetCalendarDto, member);

        return calendar;
    }

    @Counted("diary.api.count")
    public void writeDiary(String socialId, ReqSaveDiaryDto reqSaveDiaryDto) {
        // socialId 로 유저 조회
        Member member = memberRepository.findMember(socialId);

        // 일기 작성 가능성 검증
        checkDiaryWritable(member, reqSaveDiaryDto);

        // 일기 생성
        List<Emotion> findEmotions = emotionRepository.findEmotionsIn(reqSaveDiaryDto.getEmotions());
        Diary diary = Diary.createDiary(member, reqSaveDiaryDto, findEmotions);

        // 일기 저장
        diaryRepository.save(diary);
    }
    @Counted("diary.api.count")
    @Transactional(readOnly = true)
    public RespGetDiaryDto getDiary(String socialId, ReqGetDiaryDto reqGetDiaryDto) {
        // socialId 로 유저 조회
        Member member = memberRepository.findMember(socialId);

        // 조회할 일기 가져오기
        Diary selectDiary = diaryRepository.findDiary(member.getMemberId(), reqGetDiaryDto.getDate());

        // 조회한 일기로 반환값 생성
        return RespGetDiaryDto.createRespGetDiaryDto(selectDiary);
    }

    @Counted("diary.api.count")
    public void updateDiary(String socialId, ReqEditDiaryDto reqEditDiary) {
        // socialId 로 유저 조회
        Member member = memberRepository.findMember(socialId);
        List<Emotion> findEmotions = emotionRepository.findEmotionsIn(reqEditDiary.getEmotions());

        // 수정할 일기 조회
        Diary updateDiary = diaryRepository.findDiary(member.getMemberId(), reqEditDiary.getDate());

        // 일기 수정 가능 여부 체크
        updateDiary.checkUpdatable();

        // 일기 수정
        updateDiary.updateDiary(reqEditDiary, findEmotions);
    }

    @Counted("diary.api.count")
    public void deleteDiary(String socialId, ReqDeleteDiaryDto reqDeleteDiaryDto) {
        // socialId 로 유저 조회
        Member member = memberRepository.findMember(socialId);

        // 삭제할 일기 가져오기
        Diary deleteDiary = diaryRepository.findDiary(member.getMemberId(), reqDeleteDiaryDto.getDate());

        // 기존 일기 삭제
        diaryRepository.delete(deleteDiary);
    }

    @Counted("diary.api.count")
    @Transactional(readOnly = true)
    public RespGetEmotionDto getEmotion() {
        // 감정어 조회
        RespGetEmotionDto emotions = RespGetEmotionDto.createRespGetEmotionDto(emotionRepository.findAll());

        return emotions;
    }


    //===편의 메서드===//

    private RespGetCalendarDto getRespGetCalendarDto(ReqGetCalendarDto reqGetCalendarDto, Member member) {
        // 요청된 기간내 유저의 기록이 존재하는 날 조회
        List<LocalDateTime> existDiaryDate = diaryRepository.findExistDiaryDateList(
                member.getMemberId(),
                reqGetCalendarDto.getFromDate(),
                reqGetCalendarDto.getToDate());

        // 회고 요일과 현재 날짜로 일기 작성 가능주 구하기
        LocalDateTime now = LocalDateTime.now();
        DiaryWritableWeek diaryWritableWeek = DiaryWritableWeek.create(member.getRetrospectDay(), now);

        List<RetrospectInfoDto> retrospectInfoList = retrospectRepository.findRetrospectList(member.getMemberId(),
                reqGetCalendarDto.getFromDate(),
                reqGetCalendarDto.getToDate());

        // 회고 작성 여부
        Boolean existRetrospect = retrospectRepository.existDiaryDate(
                member.getMemberId(),
                diaryWritableWeek.getRetroDate());

        return RespGetCalendarDto.builder()
                .nickname(member.getNickname())
                .isRetrospectDay(member.isRetrospectDay(now))
                .existRetrospect(existRetrospect)
                .existDiaryDate(existDiaryDate)
                .nextDayOfPrevRetroDate(diaryWritableWeek.getNextDayOfPrevRetroDate())
                .retroDate(diaryWritableWeek.getRetroDate())
                .retrospectInfoList(retrospectInfoList)
                .build();
    }


    private void checkDiaryWritable(Member member, ReqDiaryDto reqSaveDiaryDto) {
        // 일기 작성 가능주간인지 체크
        checkWritableWeek(member, reqSaveDiaryDto.getDate());
        // 해당 날짜에 작성한 회고 존재하는지 체크
        retrospectRepository.checkRetrospectAlreadyExist(member.getMemberId(), reqSaveDiaryDto.getDate());
        // 해당 날짜에 작성한 일기 존재하는지 체크
        diaryRepository.checkTodayDiaryAlreadyExist(member.getMemberId(), reqSaveDiaryDto.getDate());
    }

    private void checkWritableWeek(Member member, LocalDateTime date) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextDayOfPrevRetroDate = DiaryWritableWeek.getNextDayOfPrevRetroDate(member.getRetrospectDay(), now);
        // validation 을 통해 현재보다 미래의 날짜가 들어오지 않는 것을 보장
        if(!(date.isEqual(nextDayOfPrevRetroDate) || date.isAfter(nextDayOfPrevRetroDate))) throw NotInDiaryWritableDateException.EXCEPTION;
    }
}
