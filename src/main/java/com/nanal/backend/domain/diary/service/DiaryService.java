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
import com.nanal.backend.domain.retrospect.entity.Retrospect;
import com.nanal.backend.domain.retrospect.repository.RetrospectRepository;
import com.nanal.backend.global.exception.customexception.MemberAuthException;
import com.nanal.backend.domain.diary.repository.DiaryRepository;
import com.nanal.backend.domain.diary.repository.EmotionRepository;
import com.nanal.backend.domain.auth.repository.MemberRepository;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

    @Transactional(readOnly = true)
    public RespGetCalendarDto getCalendar(String socialId, ReqGetCalendarDto reqGetCalendarDto) {
        // socialId 로 유저 조회
        Member member = findMember(socialId);

        // 요청된 기간내 유저의 기록이 존재하는 날 조회
        List<LocalDateTime> existDiaryDate = getExistDiaryDateList(member.getMemberId(),
                reqGetCalendarDto.getFromDate(),
                reqGetCalendarDto.getToDate());

        // 회고 요일과 현재 날짜로 일기 작성 가능주 구하기
        LocalDateTime now = LocalDateTime.now();
        DiaryWritableWeek diaryWritableWeek = DiaryWritableWeek.create(member.getRetrospectDay(), now);

        List<RetrospectInfoDto> retrospectInfoList = getRetrospectList(member.getMemberId(),
                reqGetCalendarDto.getFromDate(),
                reqGetCalendarDto.getToDate());

        // 회고 작성 여부
        Boolean existRetrospect = existDiaryDate(member.getMemberId(), diaryWritableWeek.getRetroDate());

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


    public void writeDiary(String socialId, ReqSaveDiaryDto reqSaveDiaryDto) {
        // socialId 로 유저 조회
        Member member = findMember(socialId);

        // 일기 작성 가능성 검증
        checkDiaryWritable(member, reqSaveDiaryDto);

        List<Emotion> findEmotions = emotionRepository.findEmotionsIn(reqSaveDiaryDto.getEmotions());

        // 일기 Entity 생성
        Diary diary = Diary.createDiary(member, reqSaveDiaryDto, findEmotions);

        // 일기 저장
        diaryRepository.save(diary);
    }

    @Transactional(readOnly = true)
    public RespGetDiaryDto getDiary(String socialId, ReqGetDiaryDto reqGetDiaryDto) {
        // socialId 로 유저 조회
        Member member = findMember(socialId);

        // 조회할 일기 가져오기
        Diary selectDiary = getDiary(member.getMemberId(), reqGetDiaryDto.getDate());

        // 조회한 일기로 반환값 생성
        return RespGetDiaryDto.createRespGetDiaryDto(selectDiary);
    }

    public void updateDiary(String socialId, ReqEditDiaryDto reqEditDiary) {
        // socialId 로 유저 조회
        Member member = findMember(socialId);
        List<Emotion> findEmotions = emotionRepository.findEmotionsIn(reqEditDiary.getEmotions());

        // 수정할 일기 조회
        Diary updateDiary = getDiary(member.getMemberId(), reqEditDiary.getDate());

        // 일기 수정 가능 여부 체크
        checkUpdatable(updateDiary);

        // 일기 수정
        updateDiary.updateDiary(reqEditDiary, findEmotions);
    }

    public void deleteDiary(String socialId, ReqDeleteDiaryDto reqDeleteDiaryDto) {
        // socialId 로 유저 조회
        Member member = findMember(socialId);

        // 삭제할 일기 가져오기
        Diary deleteDiary = getDiary(member.getMemberId(), reqDeleteDiaryDto.getDate());

        // 기존 일기 삭제
        diaryRepository.delete(deleteDiary);
    }

    @Transactional(readOnly = true)
    public RespGetEmotionDto getEmotion() {
        // 감정어 조회
        RespGetEmotionDto respGetEmotionDto = getRespGetEmotionDto(emotionRepository.findAll());

        return respGetEmotionDto;
    }


    //===편의 메서드===//

    private Diary getDiary(Long memberId, LocalDateTime date) {
        LocalDate tempDate = date.toLocalDate();
        LocalDateTime startDate = tempDate.atStartOfDay();
        LocalDateTime endDate = tempDate.atTime(LocalTime.MAX).withNano(0);

        // 선택한 날에 작성한 일기 조회
        Diary findDiary = diaryRepository.findDiaryByMemberAndWriteDate(memberId, startDate, endDate)
                .orElseThrow(() -> DiaryNotFoundException.EXCEPTION);

        return findDiary;
    }

    private static void checkUpdatable(Diary updateDiary) {
        if(!updateDiary.getEditStatus()) throw DiaryChangeUnavailable.EXCEPTION;
    }

    private void checkWritableWeek(Member member, LocalDateTime date) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextDayOfPrevRetroDate = DiaryWritableWeek.getNextDayOfPrevRetroDate(member.getRetrospectDay(), now);
        // validation 을 통해 현재보다 미래의 날짜가 들어오지 않는 것을 보장
        if(!(date.isEqual(nextDayOfPrevRetroDate) || date.isAfter(nextDayOfPrevRetroDate))) throw NotInDiaryWritableDateException.EXCEPTION;
    }

    private void checkRetrospectAlreadyExist(Long memberId, LocalDateTime date) {
        LocalDate tempDate = date.toLocalDate();
        LocalDateTime startDate = tempDate.atStartOfDay();
        LocalDateTime endDate = tempDate.atTime(LocalTime.MAX).withNano(0);

        Optional<Retrospect> findRetrospect = retrospectRepository.findByMemberAndWriteDate(memberId, startDate, endDate);

        if(findRetrospect.isPresent()) throw RetrospectAlreadyWrittenException.EXCEPTION;
    }

    private void checkDiaryAlreadyExist(Long memberId, LocalDateTime date) {
        LocalDate tempDate = date.toLocalDate();
        LocalDateTime startDate = tempDate.atStartOfDay();
        LocalDateTime endDate = tempDate.atTime(LocalTime.MAX).withNano(0);

        List<Diary> findDiaryList = diaryRepository.findDiaryListByMemberAndWriteDate(memberId, startDate, endDate);

        if (findDiaryList.size() != 0) throw DiaryAlreadyExistException.EXCEPTION;
    }

    private Boolean existDiaryDate(Long memberId, LocalDateTime date) {
        LocalDate tempDate = date.toLocalDate();
        LocalDateTime startDate = tempDate.atStartOfDay();
        LocalDateTime endDate = tempDate.atTime(LocalTime.MAX).withNano(0);

        Optional<Retrospect> findRetrospect = retrospectRepository.findByMemberAndWriteDate(memberId, startDate, endDate);

        if(findRetrospect.isPresent()) return true;
        else return false;
    }

    private List<LocalDateTime> getExistDiaryDateList(Long memberId, LocalDateTime fromDate, LocalDateTime toDate) {
        LocalDateTime startDate = fromDate.toLocalDate().atStartOfDay();
        LocalDateTime endDate = toDate.toLocalDate().atTime(LocalTime.MAX).withNano(0);

        // 선택한 날에 작성한 일기리스트 조회
        List<Diary> writeDates = diaryRepository.findDiaryListByMemberAndWriteDate(memberId, startDate, endDate);

        // 일기리스트의 작성날짜 List 생성
        return writeDates.stream()
                .map(Diary::getWriteDate)
                .collect(Collectors.toList());
    }

    private List<RetrospectInfoDto> getRetrospectList(Long memberId, LocalDateTime fromDate, LocalDateTime toDate) {
        LocalDateTime startDate = fromDate.toLocalDate().atStartOfDay();
        LocalDateTime endDate = toDate.toLocalDate().atTime(LocalTime.MAX).withNano(0);

        // 선택한 날에 작성된 회고 조회
        List<Retrospect> retrospectList = retrospectRepository.findDiaryListByMemberAndWriteDate(memberId, startDate, endDate);

        // 회고 List 생성
        return retrospectList.stream()
                .map(RetrospectInfoDto::new)
                .collect(Collectors.toList());
    }

    private RespGetEmotionDto getRespGetEmotionDto(List<Emotion> emotions) {
        List<String> emotionWords = emotions.stream()
                .map(Emotion::getEmotion)
                .collect(Collectors.toList());

        return new RespGetEmotionDto(emotionWords);
    }

    private Member findMember(String socialId) {
        return memberRepository.findBySocialId(socialId).orElseThrow(() -> MemberAuthException.EXCEPTION);
    }

    private void checkDiaryWritable(Member member, ReqDiaryDto reqSaveDiaryDto) {
        // 일기 작성 가능주간인지 체크
        checkWritableWeek(member, reqSaveDiaryDto.getDate());
        // 해당 날짜에 작성한 회고 존재하는지 체크
        checkRetrospectAlreadyExist(member.getMemberId(), reqSaveDiaryDto.getDate());
        // 해당 날짜에 작성한 일기 존재하는지 체크
        checkDiaryAlreadyExist(member.getMemberId(), reqSaveDiaryDto.getDate());
    }
}
