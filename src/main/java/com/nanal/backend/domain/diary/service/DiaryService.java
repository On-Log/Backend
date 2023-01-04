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

    public void saveDiary(String email, ReqSaveDiaryDto reqSaveDiaryDto) {
        // email 로 유저 조회
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberAuthException("존재하지 않는 유저입니다."));

        // 해당 날짜에 작성한 일기 존재하는지 체크
        // 질의할 sql 의 Like 절에 해당하게끔 변환
        String yearMonthDay = reqSaveDiaryDto.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "%";
        // 선택한 yyyy-MM-dd 에 작성한 일기 조회
        List<Diary> existDiary = diaryRepository.findDiaryListByMemberAndWriteDate(member.getMemberId(), yearMonthDay);
        if(existDiary.size() == 1) throw new DiaryAlreadyExistException("이미 해당 날짜에 작성한 일기가 존재합니다.");

        // 일기 Entity 생성
        Diary diary = createDiary(member, reqSaveDiaryDto.getContent(), reqSaveDiaryDto.getDate(), reqSaveDiaryDto.getKeywords());

        // 일기 저장
        diaryRepository.save(diary);
    }

    public RespGetCalendarDto getCalendar(String email, ReqGetCalendarDto reqGetCalendarDto) {
        // email 로 유저 조회
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberAuthException("존재하지 않는 유저입니다."));

        LocalDateTime currentDate = reqGetCalendarDto.getCurrentDate();
        LocalDateTime selectDate = reqGetCalendarDto.getSelectDate();

        /*
        현재는 like 절을 이용해서 Diary 의 전체 컬럼을 뽑아온 다음 작업하는 방식.
        추후 부등호를 이용해서 write_date 컬럼만 뽑아오는 방식으로 변환 (like 절과 부등호를 뽑아는 방식 성능 비교)
         */
        // 요청된 기간내 기록이 존재하는 날 조회
        List<Integer> existDiaryDate = getExistDiaryDate(member, selectDate);

        // 회고 요일과 현재 날짜로 일기 작성 가능주 구하기
        LocalDateTime nextDayOfPrevRetroDate = getNextDayOfPrevRetroDate(member.getRetrospectDay(), currentDate);

        LocalDateTime postRetroDate = getPostRetroDate(member.getRetrospectDay(), currentDate);

        return RespGetCalendarDto.builder()
                .existDiaryDate(existDiaryDate)
                .prevRetroDate(nextDayOfPrevRetroDate.getDayOfMonth())
                .postRetroDate(postRetroDate.getDayOfMonth())
                .build();
    }

    public RespGetEmotionDto getEmotion() {
        // 감정어 조회
        List<Emotion> emotions = emotionRepository.findAll();

        RespGetEmotionDto respGetEmotionDto = getRespGetEmotionDto(emotions);

        return respGetEmotionDto;
    }

    public RespGetDiaryDto getDiary(String email, ReqGetDiaryDto reqGetDiaryDto) {
        // email 로 유저 조회
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberAuthException("존재하지 않는 유저입니다."));
        System.out.println(reqGetDiaryDto.getDate());
        // 질의할 sql 의 Like 절에 해당하게끔 변환
        String yearMonthDay = reqGetDiaryDto.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "%";
        // 선택한 yyyy-MM-dd 에 작성한 일기 조회
        Diary selectDiary = diaryRepository.findDiaryByMemberAndWriteDate(member.getMemberId(), yearMonthDay)
                .orElseThrow(() -> new DiaryNotFoundException("해당 날짜에 작성한 일기가 존재하지 않습니다."));

        // 조회한 일기로 반환값 생성
        RespGetDiaryDto respGetDiaryDto = RespGetDiaryDto.makeRespGetDiaryDto(selectDiary);

        return respGetDiaryDto;
    }

    public void editDiary(String email, ReqEditDiaryDto reqEditDiary) {
        // email 로 유저 조회
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberAuthException("존재하지 않는 유저입니다."));

        /*
        현재는 수정요청 들어오면 기존 일기삭제 후, 다시 저장하는 방식
        추후에 더 효율적인 방법으로 수정 필요
         */
        // 질의할 sql 의 Like 절에 해당하게끔 변환
        String yearMonthDay = reqEditDiary.getEditDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "%";
        // 선택한 yyyy-MM-dd 에 작성한 일기 조회
        Diary selectDiary = diaryRepository.findDiaryByMemberAndWriteDate(member.getMemberId(), yearMonthDay)
                .orElseThrow(() -> new DiaryNotFoundException("해당 날짜에 작성한 일기가 존재하지 않습니다."));
        // 기존 일기 삭제
        diaryRepository.delete(selectDiary);

        // 새로운 일기 Entity 생성
        Diary diary = createDiary(member, reqEditDiary.getContent(), reqEditDiary.getEditDate(), reqEditDiary.getKeywords());
        // 일기 저장
        diaryRepository.save(diary);
    }

    public void deleteDiary(String email, ReqDeleteDiaryDto reqDeleteDiaryDto) {
        // email 로 유저 조회
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberAuthException("존재하지 않는 유저입니다."));

        // 질의할 sql 의 Like 절에 해당하게끔 변환
        String yearMonthDay = reqDeleteDiaryDto.getDeleteDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "%";

        /*
        일기와 해당 일기의 키워드, 감정어등을 삭제하고자 할 때, 일기 Entity 를 가져온 다음에 해당 Entity 를 삭제하는 식으로 이루어 짐.
        추후에 한 번의 쿼리만으로 Cascade 로직이 정상적으로 작동할 수 있도록 수정 필요
        diaryRepository.deleteByMemberAndWriteDate(member.getMemberId(), reqDeleteDiaryDto.getDeleteDate());
         */
        // 선택한 yyyy-MM-dd 에 작성한 일기 조회
        Diary selectDiary = diaryRepository.findDiaryByMemberAndWriteDate(member.getMemberId(), yearMonthDay)
                .orElseThrow(() -> new DiaryNotFoundException("해당 날짜에 작성한 일기가 존재하지 않습니다."));
        // 기존 일기 삭제
        diaryRepository.delete(selectDiary);
    }





    //===편의 메서드===//

    private Diary createDiary(Member member, String content, LocalDateTime date, List<KeywordDto> keywordDtos) {
        // Diary 생성에 필요한 Keyword 리스트 생성
        List<Keyword> keywords = new ArrayList<>();

        for (KeywordDto keywordDto : keywordDtos) {

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
        Diary diary = Diary.makeDiary(member, keywords, content, date);

        return diary;
    }

    private List<Integer> getExistDiaryDate(Member member, LocalDateTime selectTime) {
        // 질의할 sql 의 Like 절에 해당하게끔 변환
        String yearMonth = selectTime.format(DateTimeFormatter.ofPattern("yyyy-MM")) + "%";

        // 선택한 yyyy-MM 에 작성한 일기리스트 조회
        List<Diary> writeDates = diaryRepository.findListByMemberAndWriteDate(
                member.getMemberId(),
                yearMonth);

        // 가져온 작성날짜 일 단위로 파싱해서 List 삽입
        List<Integer> existDiaryDate = new ArrayList<>();
        for (Diary t : writeDates) {
            existDiaryDate.add(t.getWriteDate().getDayOfMonth());
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
