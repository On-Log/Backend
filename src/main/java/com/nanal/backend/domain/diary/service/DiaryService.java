package com.nanal.backend.domain.diary.service;

import com.nanal.backend.domain.diary.dto.*;
import com.nanal.backend.domain.diary.repository.DiaryRepository;
import com.nanal.backend.domain.diary.repository.EmotionRepository;
import com.nanal.backend.domain.diary.repository.KeywordRepository;
import com.nanal.backend.domain.oauth.repository.MemberRepository;
import com.nanal.backend.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    public void saveDiary(String email, ReqSaveDiaryDto reqSaveDiaryDto) {
        // email 로 유저 조회
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new RuntimeException());

        // 일기 저장
        createDiary(member, reqSaveDiaryDto);
    }

    public RespGetCalendarDto getCalendar(String email, ReqGetCalendarDto reqGetCalendarDto) {
        // email 로 유저 조회
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new RuntimeException());

        LocalDateTime currentDate = reqGetCalendarDto.getCurrentDate();
        LocalDateTime selectDate = reqGetCalendarDto.getSelectDate();

        /*
        현재는 like 절을 이용해서 Diary 의 전체 컬럼을 뽑아온 다음 작업하는 방식.
        추후 부등호를 이용해서 write_date 컬럼만 뽑아오는 방식으로 변환 (like 절과 부등호를 뽑아는 방식 성능 비교)
         */

        // 요청된 기간내 기록이 존재하는 날 조회
        List<Integer> existDiaryDate = getExistDiaryDate(member, selectDate);

        // 회고 요일과 현재 날짜 로 일기 작성 가능주 구하기
        LocalDateTime nextDayOfPrevRetroDate = getNextDayOfPrevRetroDate(member.getRetrospectDay(), currentDate);
        LocalDateTime postRetroDate = getPostRetroDate(member.getRetrospectDay(), currentDate);

        return new RespGetCalendarDto(existDiaryDate, nextDayOfPrevRetroDate.getDayOfMonth(), postRetroDate.getDayOfMonth());
    }

    public RespGetEmotionDto getEmotion() {
        // 감정어 조회
        List<Emotion> emotions = emotionRepository.findAll();

        RespGetEmotionDto respGetEmotionDto = getRespGetEmotionDto(emotions);

        return respGetEmotionDto;
    }




    //===편의 메서드===//
    private void createDiary(Member member, ReqSaveDiaryDto reqSaveDiaryDto) {
        // Diary 생성에 필요한 Keyword 리스트 생성
        List<Keyword> keywords = new ArrayList<>();

        for (KeywordDto keywordDto : reqSaveDiaryDto.getKeywords()) {

            // Keyword 생성에 필요한 KeywordEmotion 리스트 생성
            List<KeywordEmotion> keywordEmotions = new ArrayList<>();

            // KeywordEmotion 리스트에 KeywordEmotion 생성하여 삽입.
            for(KeywordEmotionDto keywordEmotionDto : keywordDto.getKeywordEmotions()){
                KeywordEmotion keywordEmotion = KeywordEmotion.createKeywordEmotion(keywordEmotionDto.getEmotion());
                keywordEmotions.add(keywordEmotion);
            }

            // Keyword 리스트에 KeywordEmotion 리스트를 이용하여 생성한 Keyword 삽입.
            Keyword keyword = Keyword.createKeyword(keywordDto.getKeyword(), keywordEmotions);
            keywords.add(keyword);
        }

        // Keyword 리스트를 이용하여 Diary 생성
        Diary diary = Diary.createDiary(member, keywords, reqSaveDiaryDto.getContent(), reqSaveDiaryDto.getDate());

        // Diary 저장
        diaryRepository.save(diary);
    }

    private List<Integer> getExistDiaryDate(Member member, LocalDateTime selectTime) {
        // 질의할 sql 의 Like 절에 해당하게끔 변환
        String yearMonth = selectTime.format(DateTimeFormatter.ofPattern("yyyy-MM")) + "%";

        // 선택한 yyyy-MM 에 존재하는 작성날짜 가져오기
        List<Diary> writeDates = diaryRepository.findByMemberAndWriteDate(
                member.getMemberId(),
                yearMonth);

        // 가져온 작성날짜 일 단위로 파싱해서 List 삽입
        List<Integer> existDiaryDate = new ArrayList<>();
        for (Diary t : writeDates) {
            existDiaryDate.add(t.getWriteDate().getDayOfMonth());
        }
        return existDiaryDate;
    }

    private LocalDateTime getPostRetroDate(DayOfWeek retrospectDay, LocalDateTime currentTime) {
        // 다음 회고일
        return currentTime.with(TemporalAdjusters.nextOrSame(retrospectDay));
    }

    private LocalDateTime getNextDayOfPrevRetroDate(DayOfWeek retrospectDay, LocalDateTime currentTime) {
        // 이전 회고일
        LocalDateTime prevRetroDate = currentTime.with(TemporalAdjusters.previousOrSame(retrospectDay));
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
