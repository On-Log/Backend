package com.nanal.backend.domain.retrospect.service;

import com.nanal.backend.config.exception.customexception.MemberAuthException;
import com.nanal.backend.domain.diary.repository.DiaryRepository;
import com.nanal.backend.domain.diary.service.DiaryService;
import com.nanal.backend.domain.mypage.repository.MemberRepository;
import com.nanal.backend.domain.retrospect.dto.*;
import com.nanal.backend.domain.retrospect.repository.RetrospectKeywordRepository;
import com.nanal.backend.domain.retrospect.repository.RetrospectQuestionRepository;
import com.nanal.backend.domain.retrospect.repository.RetrospectRepository;
import com.nanal.backend.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@EnableScheduling
@RequiredArgsConstructor
@Transactional
@Service
public class RetrospectService {

    private final MemberRepository memberRepository;
    private final DiaryRepository diaryRepository;
    private final RetrospectRepository retrospectRepository;
    private final DiaryService diaryService;
    private final RetrospectKeywordRepository retrospectKeywordRepository;
    private final RetrospectQuestionRepository retrospectQuestionRepository;

    @Transactional(readOnly = true)
    public RespGetInfoDto getInfo(String email, ReqGetInfoDto reqGetInfoDto) {
        // email 로 유저 조회
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberAuthException("회고 탭 화면 요청"));
        LocalDateTime currentDate = reqGetInfoDto.getCurrentDate();
        LocalDateTime selectDate = reqGetInfoDto.getSelectDate();

        // 선택한 월에 있는 회고 기록 ( 어떤 회고 목적을 선택했는가 )
        List<Retrospect> getRetrospects = getExistRetrospect(member, selectDate);
        List<String> existRetrospect = new ArrayList<>();
        for (Retrospect t : getRetrospects) {
            existRetrospect.add(t.getGoal());
        }

        LocalDateTime postRetroDate = diaryService.getPostRetroDate(member.getRetrospectDay(), currentDate);
        // 회고 요일까지 남은 날짜
        Period period = Period.between(currentDate.toLocalDate(), postRetroDate.toLocalDate());
        // 회고 주제별로 분류 후 주차별로 분류
        List<List<List<String>>> existRetrospectKeyword = getKeyword(member, selectDate);

        return RespGetInfoDto.builder()
                .existRetrospect(existRetrospect)
                .betweenDate(period.getDays())
                .existRetrospectKeyword(existRetrospectKeyword)
                .build();
    }


    public void saveRetrospect(String email, ReqSaveRetroDto reqSaveRetroDto) {
        // email 로 유저 조회
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberAuthException("회고 기록 요청"));
        // 회고 Entity 생성
        Retrospect retrospect = createRetrospect(member, reqSaveRetroDto.getGoal(), reqSaveRetroDto.getDate(), reqSaveRetroDto.getKeywords(), reqSaveRetroDto.getContents());
        // 회고 저장
        retrospectRepository.save(retrospect);
    }


    @Transactional
    public RespGetRetroDto getRetro(String email, ReqGetRetroDto reqGetRetroDto) {
        // email 로 유저 조회
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberAuthException("회고 조회 요청"));

        LocalDateTime currentTime = reqGetRetroDto.getCurrentDate();
        LocalDateTime selectDate = reqGetRetroDto.getSelectDate();
        // 선택한 yyyy-MM 에 작성한 회고리스트 조회
        List<Retrospect> getRetrospects = getExistRetrospect(member, selectDate);

        // 몇번째 회고인지 조회한 후, 회고 리스트로 반환값 생성
        RespGetRetroDto respGetRetroDto = RespGetRetroDto.makeRespGetRetroDto(getRetrospects.get(reqGetRetroDto.getWeek()));

        // 수정 기간 지나면 수정 못하게 editStatus 변경
        LocalDateTime prevRetroDate = currentTime.with(TemporalAdjusters.previousOrSame(member.getRetrospectDay()));
        if(currentTime.isAfter(prevRetroDate.plusDays(1).withHour(7).withMinute(00).withSecond(00))==true) {
            getRetrospects.get(reqGetRetroDto.getWeek()).updateEditStatus(false);
        }

        return respGetRetroDto;
    }


    public void editRetrospect(String email, ReqEditRetroDto reqEditRetroDto) {
        // email 로 유저 조회
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberAuthException("회고 수정 요청"));

        LocalDateTime selectDate = reqEditRetroDto.getEditDate();
        // 선택한 yyyy-MM 에 작성한 회고리스트 조회
        List<Retrospect> getRetrospects = getExistRetrospect(member, selectDate);
        // 몇번째 회고인지
        Retrospect retrospect = getRetrospects.get(reqEditRetroDto.getWeek());
        // 회고에서 어떤 질문에 대한 답을 수정했는지
        List<RetrospectContent> retrospectContents = retrospect.getRetrospectContents();
        // 내용 수정
        retrospectContents.get(reqEditRetroDto.getIndex()).changeAnswer(reqEditRetroDto.getAnswer());
    }


    public RespGetKeywordAndEmotionDto getKeywordAndEmotion(String email, ReqGetKeywordAndEmotionDto reqGetKeywordAndEmotionDto){
        // email 로 유저 조회
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberAuthException("일기 작성 날짜+키워드+감정어 조회"));

        LocalDateTime currentTime = reqGetKeywordAndEmotionDto.getCurrentDate();
        LocalDateTime prevRetroDate = currentTime.with(TemporalAdjusters.previousOrSame(member.getRetrospectDay()));

        //일주일 일기 리스트 조회
        List<Diary> diaries = diaryRepository.findListByMemberAndBetweenWriteDate(member.getMemberId(), prevRetroDate.toLocalDate().minusDays(7), currentTime.toLocalDate());

        RespGetKeywordAndEmotionDto respGetKeywordAndEmotionDto = RespGetKeywordAndEmotionDto.makeRespGetKeywordAndEmotionDto(diaries);

        return respGetKeywordAndEmotionDto;
    }


    @Transactional(readOnly = true)
    public RespGetQuestionAndHelpDto getQuestionAndHelp() {
        // 감정어 조회
        List<RetrospectQuestion> retrospectQuestions = retrospectQuestionRepository.findAll();

        RespGetQuestionAndHelpDto respGetQuestionAndHelpDto = getRespGetQuestionAndHelpDto(retrospectQuestions);

        return respGetQuestionAndHelpDto;
    }


    //===편의 메서드===//
    private Retrospect createRetrospect(Member member, String goal, LocalDateTime date, List<RetrospectKeywordDto> keywordDtos, List<RetrospectContentDto> contentDtos) {
        // Retrospect 생성에 필요한 keyword, content 리스트 생성
        List<RetrospectKeyword> keywords = new ArrayList<>();
        List<RetrospectContent> contents = new ArrayList<>();

        for (RetrospectKeywordDto retrospectKeywordDto : keywordDtos) {
            RetrospectKeyword retrospectKeyword = RetrospectKeyword.makeRetrospectKeyword(retrospectKeywordDto.getKeyword(), retrospectKeywordDto.getClassify());
            keywords.add(retrospectKeyword);
        }

        for (RetrospectContentDto retrospectContentDto : contentDtos) {
            RetrospectContent retrospectContent = RetrospectContent.makeRetrospectContent(retrospectContentDto.getQuestion(), retrospectContentDto.getAnswer());
            contents.add(retrospectContent);
        }

        // keyword 리스트와 content리스트 이용하여 Retrospect 생성
        Retrospect retrospect = Retrospect.makeRetrospect(member, keywords,contents, goal, date);

        return retrospect;
    }


    private List<Retrospect> getExistRetrospect(Member member, LocalDateTime selectTime) {
        System.out.println(selectTime);
        // 질의할 sql 의 Like 절에 해당하게끔 변환
        String yearMonth = selectTime.format(DateTimeFormatter.ofPattern("yyyy-MM")) + "%";

        // 선택한 yyyy-MM 에 작성한 회고 리스트 조회
        List<Retrospect> retrospects = retrospectRepository.findListByMemberAndWriteDate(
                member.getMemberId(),
                yearMonth);

        return retrospects;
    }

    private List<List<List<String>>> getKeyword(Member member, LocalDateTime selectTime){
        // 전체 분할한 키워드 리스트들
        // 분류1 : [
        //   1차 : [
        //   ]
        //   2차 : [
        //   ]
        //   3차 : [
        //   ]
        //]
        List<List<List<String>>> keywordList = new ArrayList<>();

        //회고별 분류
        //   1차 : [
        //   ]
        //   2차 : [
        //   ]
        //   3차 : [
        //   ]

        // 질의할 sql 의 Like 절에 해당하게끔 변환
        String yearMonth = selectTime.format(DateTimeFormatter.ofPattern("yyyy-MM")) + "%";

        // 선택한 yyyy-MM 에 작성한 회고 리스트 조회
        List<Retrospect> writeRetrospect = retrospectRepository.findListByMemberAndWriteDate(
                member.getMemberId(),
                yearMonth);

        //회고의 분류 리스트 생성
        List<String> keyWordClass = new ArrayList<>();
        keyWordClass.add("그때 그대로 의미있었던 행복한 기억");
        keyWordClass.add("나를 힘들게 했지만 도움이 된 기억");
        keyWordClass.add("돌아보니, 다른 의미로 다가온 기억");
        keyWordClass.add("놓아줘도 괜찮은 기억");

        for (int i = 0; i < keyWordClass.size(); i++) {
            List<List<String>> klist = new ArrayList<>();
            for (Retrospect t : writeRetrospect) {
                List<RetrospectKeyword> classifiedKeyword = retrospectKeywordRepository.findListByRetroAndClassify(t.getRetrospectId(), keyWordClass.get(i));
               //t번째 회고의 i번째 분류 키워드
                List<String> keyword = new ArrayList<>();
                for (RetrospectKeyword r : classifiedKeyword) {
                    keyword.add(r.getKeyword());
                }
                klist.add(keyword);
            }
            keywordList.add(klist);
        }

        return keywordList;
    }
    private RespGetQuestionAndHelpDto getRespGetQuestionAndHelpDto(List<RetrospectQuestion> retrospectQuestions) {
        List<String> questionAndHelp = new ArrayList<>();

        for (RetrospectQuestion t : retrospectQuestions) {
            String str = "";
            str = t.getQuestion() + " " + t.getHelp();
            questionAndHelp.add(str);
        }

        RespGetQuestionAndHelpDto respGetQuestionAndHelpDto = new RespGetQuestionAndHelpDto();
        respGetQuestionAndHelpDto.setQuestionAndHelp(questionAndHelp);
        return respGetQuestionAndHelpDto;
    }
}
