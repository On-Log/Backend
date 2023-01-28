package com.nanal.backend.domain.diary.service;

import com.nanal.backend.domain.diary.dto.req.*;
import com.nanal.backend.domain.diary.dto.resp.RespGetCalendarDto;
import com.nanal.backend.domain.diary.dto.resp.RespGetDiaryDto;
import com.nanal.backend.domain.diary.dto.resp.RespGetEmotionDto;
import com.nanal.backend.domain.diary.entity.Diary;
import com.nanal.backend.domain.diary.entity.Emotion;
import com.nanal.backend.domain.diary.entity.Keyword;
import com.nanal.backend.domain.diary.entity.KeywordEmotion;
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
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class DiaryService {

    private final MemberRepository memberRepository;
    private final DiaryRepository diaryRepository;
    private final EmotionRepository emotionRepository;

    public RespGetCalendarDto getCalendar(String socialId, ReqGetCalendarDto reqGetCalendarDto) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> new MemberAuthException(ErrorCode.MEMBER_NOT_FOUND.getMessage()));

        // 요청된 기간내 유저의 기록이 존재하는 날 조회
        List<LocalDateTime> existDiaryDate = getExistDiaryDateList(member.getMemberId(), reqGetCalendarDto.getSelectDate());

        // 회고 요일과 현재 날짜로 일기 작성 가능주 구하기
        LocalDateTime nextDayOfPrevRetroDate = getNextDayOfPrevRetroDate(member.getRetrospectDay(), reqGetCalendarDto.getCurrentDate());
        LocalDateTime postRetroDate = getPostRetroDate(member.getRetrospectDay(), reqGetCalendarDto.getCurrentDate());

        return RespGetCalendarDto.builder()
                .existDiaryDate(existDiaryDate)
                .nextDayOfPrevRetroDate(nextDayOfPrevRetroDate)
                .postRetroDate(postRetroDate)
                .build();
    }

    public void saveDiary(String socialId, ReqSaveDiaryDto reqSaveDiaryDto) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> new MemberAuthException(ErrorCode.MEMBER_NOT_FOUND.getMessage()));

        // 해당 날짜에 작성한 일기 존재하는지 체크
        checkDiaryAlreadyExist(reqSaveDiaryDto, member);

        // 일기 Entity 생성
        Diary diary = createDiary(member, reqSaveDiaryDto);

        // 일기 저장
        diaryRepository.save(diary);
    }

    public RespGetDiaryDto getDiary(String socialId, ReqGetDiaryDto reqGetDiaryDto) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> new MemberAuthException(ErrorCode.MEMBER_NOT_FOUND.getMessage()));

        // 조회할 일기 가져오기
        Diary selectDiary = getSelectDiary(reqGetDiaryDto.getDate(), member.getMemberId());

        // 조회한 일기로 반환값 생성
        RespGetDiaryDto respGetDiaryDto = RespGetDiaryDto.makeRespGetDiaryDto(selectDiary);

        return respGetDiaryDto;
    }

    public void editDiary(String socialId, ReqEditDiaryDto reqEditDiary) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> new MemberAuthException(ErrorCode.MEMBER_NOT_FOUND.getMessage()));

        /*
        현재는 수정요청 들어오면 기존 일기삭제 후, 다시 저장하는 방식
        추후에 더 효율적인 방법으로 수정 필요
         */

        // 삭제할 일기 가져오기
        Diary selectDiary = getSelectDiary(reqEditDiary.getDate(), member.getMemberId());
        // 기존 일기 삭제
        diaryRepository.delete(selectDiary);

        // 새로운 일기 Entity 생성
        Diary diary = createDiary(member, reqEditDiary);
        // 일기 저장
        diaryRepository.save(diary);
    }

    public void deleteDiary(String socialId, ReqDeleteDiaryDto reqDeleteDiaryDto) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> new MemberAuthException(ErrorCode.MEMBER_NOT_FOUND.getMessage()));

        /*
        일기와 해당 일기의 키워드, 감정어등을 삭제하고자 할 때, 일기 Entity 를 가져온 다음에 해당 Entity 를 삭제하는 식으로 이루어 짐.
        추후에 한 번의 쿼리만으로 Cascade 로직이 정상적으로 작동할 수 있도록 수정 필요
        diaryRepository.deleteByMemberAndWriteDate(member.getMemberId(), reqDeleteDiaryDto.getDeleteDate());
         */

        // 삭제할 일기 가져오기
        Diary selectDiary = getSelectDiary(reqDeleteDiaryDto.getDate(), member.getMemberId());
        // 기존 일기 삭제
        diaryRepository.delete(selectDiary);
    }

    public RespGetEmotionDto getEmotion() {
        // 감정어 조회
        RespGetEmotionDto respGetEmotionDto = getRespGetEmotionDto(emotionRepository.findAll());

        return respGetEmotionDto;
    }


    //===편의 메서드===//

    private Diary getSelectDiary(LocalDateTime date, Long memberId) {
        // 질의할 sql 의 Like 절에 해당하게끔 변환
        String yearMonthDay = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "%";

        // 선택한 yyyy-MM-dd 에 작성한 일기 조회
        Diary selectDiary = diaryRepository.findDiaryByMemberAndWriteDate(memberId, yearMonthDay)
                .orElseThrow(() -> new DiaryNotFoundException(ErrorCode.DIARY_NOT_FOUND.getMessage()));

        return selectDiary;
    }

    private void checkDiaryAlreadyExist(ReqSaveDiaryDto reqSaveDiaryDto, Member member) {
        // 질의할 sql 의 Like 절에 해당하게끔 변환
        String yearMonthDay = reqSaveDiaryDto.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "%";
        // 선택한 yyyy-MM-dd 에 작성한 일기 조회
        List<Diary> existDiary = diaryRepository.findDiaryListByMemberAndWriteDate(member.getMemberId(), yearMonthDay);

        if(existDiary.size() != 0) throw new DiaryAlreadyExistException(ErrorCode.DIARY_ALREADY_EXIST.getMessage());
    }

    private Diary createDiary(Member member, ReqDiaryDto reqDiaryDto) {

        // Diary 생성에 필요한 Keyword 리스트 생성
        List<Keyword> keywords = new ArrayList<>();

        for (KeywordDto keywordDto : reqDiaryDto.getKeywords()) {

            // Keyword 생성에 필요한 KeywordEmotion 리스트 생성
            List<KeywordEmotion> keywordEmotions = new ArrayList<>();

            // KeywordEmotion 리스트에 KeywordEmotion 생성하여 삽입.
            for(KeywordEmotionDto keywordEmotionDto : keywordDto.getKeywordEmotions()){
                KeywordEmotion keywordEmotion = KeywordEmotion.makeKeywordEmotion(keywordEmotionDto.getEmotion());
                keywordEmotions.add(keywordEmotion);
            }

            // Keyword 리스트에 KeywordEmotion 리스트를 이용하여 생성한 Keyword 삽입.
            Keyword keyword = Keyword.makeKeyword(keywordDto.getKeyword(), keywordEmotions);
            keywords.add(keyword);
        }

        // Keyword 리스트를 이용하여 Diary 생성
        Diary diary = Diary.makeDiary(member, keywords, reqDiaryDto.getContent(), reqDiaryDto.getDate());


        return diary;
    }

    private List<LocalDateTime> getExistDiaryDateList(Long memberId, LocalDateTime selectTime) {
        // 질의할 sql 의 Like 절에 해당하게끔 변환
        String yearMonth = selectTime.format(DateTimeFormatter.ofPattern("yyyy-MM")) + "%";

        // 선택한 yyyy-MM 에 작성한 일기리스트 조회
        List<Diary> writeDates = diaryRepository.findListByMemberAndWriteDate(memberId, yearMonth);

        // 가져온 작성날짜 일 단위로 파싱해서 List 삽입
        List<LocalDateTime> existDiaryDate = new ArrayList<>();
        for (Diary t : writeDates) {
            existDiaryDate.add(t.getWriteDate());
        }
        return existDiaryDate;
    }

    public LocalDateTime getPostRetroDate(DayOfWeek retrospectDay, LocalDateTime currentTime) {
        // 다음 회고일
        return currentTime.with(TemporalAdjusters.nextOrSame(retrospectDay));
    }

    private LocalDateTime getNextDayOfPrevRetroDate(DayOfWeek retrospectDay, LocalDateTime currentTime) {
        // 이전 회고일
        LocalDateTime prevRetroDate;
        if(currentTime.toLocalDate().isEqual(LocalDate.now()))
            prevRetroDate = currentTime.with(TemporalAdjusters.previous(retrospectDay));
        else
            prevRetroDate = currentTime.with(TemporalAdjusters.previousOrSame(retrospectDay));
        // 해당 주는 이전 회고일 다음날부터 다음 회고 일까지이므로 '이전 회고일 + 1' 을 해줘야함
        return prevRetroDate.plusDays(1);
    }

    private RespGetEmotionDto getRespGetEmotionDto(List<Emotion> emotions) {
        List<String> emotionWords = new ArrayList<>();
        for (Emotion t : emotions) {
            emotionWords.add(t.getEmotion());
        }

        RespGetEmotionDto respGetEmotionDto = new RespGetEmotionDto();
        respGetEmotionDto.setEmotion(emotionWords);
        return respGetEmotionDto;
    }
}
