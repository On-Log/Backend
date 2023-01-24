package com.nanal.backend.domain.retrospect.service;

import com.nanal.backend.domain.diary.entity.Diary;
import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.domain.retrospect.dto.req.*;
import com.nanal.backend.domain.retrospect.dto.resp.*;
import com.nanal.backend.domain.retrospect.entity.Question;
import com.nanal.backend.domain.retrospect.entity.Retrospect;
import com.nanal.backend.domain.retrospect.entity.RetrospectContent;
import com.nanal.backend.domain.retrospect.entity.RetrospectKeyword;
import com.nanal.backend.domain.retrospect.repository.QuestionRepository;
import com.nanal.backend.global.exception.customexception.MemberAuthException;
import com.nanal.backend.domain.diary.repository.DiaryRepository;
import com.nanal.backend.domain.diary.service.DiaryService;
import com.nanal.backend.domain.auth.repository.MemberRepository;
import com.nanal.backend.domain.retrospect.dto.*;
import com.nanal.backend.domain.retrospect.repository.RetrospectKeywordRepository;
import com.nanal.backend.domain.retrospect.repository.RetrospectRepository;
import com.nanal.backend.domain.retrospect.exception.RetrospectNotFoundException;
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
    private final QuestionRepository questionRepository;

    public RespGetInfoDto getInfo(String socialId, ReqGetInfoDto reqGetInfoDto) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> new MemberAuthException("존재하지 않는 유저입니다."));
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
        List<RespGetClassifiedKeywordDto> respGetClassifiedKeywordDtos = getKeyword(member, selectDate);

        RespGetInfoDto respGetInfoDto = new RespGetInfoDto(existRetrospect, period.getDays(), respGetClassifiedKeywordDtos);

        return respGetInfoDto;
    }


    public void saveRetrospect(String socialId, ReqSaveRetroDto reqSaveRetroDto) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> new MemberAuthException("존재하지 않는 유저입니다."));
        // 회고 Entity 생성
        Retrospect retrospect = createRetrospect(member, reqSaveRetroDto.getGoal(), reqSaveRetroDto.getCurrentDate(), reqSaveRetroDto.getKeywords(), reqSaveRetroDto.getContents());
        // 회고 저장
        retrospectRepository.save(retrospect);
        //회고 저장 후 일주일 일기 리스트 editstatus 변경
        LocalDateTime currentTime = reqSaveRetroDto.getCurrentDate();
        LocalDateTime prevRetroDate = currentTime.with(TemporalAdjusters.previousOrSame(member.getRetrospectDay()));
        List<Diary> diaries = diaryRepository.findListByMemberAndBetweenWriteDate(member.getMemberId(), prevRetroDate.toLocalDate().minusDays(6), currentTime.toLocalDate(),true);
        for(Diary t : diaries) {
            t.changeEditStatus(false);
        }
    }


    public RespGetRetroDto getRetro(String socialId, ReqGetRetroDto reqGetRetroDto) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> new MemberAuthException("존재하지 않는 유저입니다."));

        LocalDateTime selectDate = reqGetRetroDto.getSelectDate();
        // 선택한 yyyy-MM 에 작성한 회고리스트 조회
        List<Retrospect> getRetrospects = getExistRetrospect(member, selectDate);
        // 선택한 yyyy-MM 에 작성한 회고 중, 조회하고자 하는 회고가 존재하지 않을 경우
        if(getRetrospects.size() < reqGetRetroDto.getWeek()) throw new RetrospectNotFoundException("조회하고자 하는 회고가 존재하지 않습니다.");

        // 몇번째 회고인지 조회한 후, 회고 리스트로 반환값 생성
        RespGetRetroDto respGetRetroDto = RespGetRetroDto.makeRespGetRetroDto(getRetrospects.get(reqGetRetroDto.getWeek()));
        return respGetRetroDto;
    }


    public void editRetrospect(String socialId, ReqEditRetroDto reqEditRetroDto) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> new MemberAuthException("존재하지 않는 유저입니다."));

        LocalDateTime selectDate = reqEditRetroDto.getEditDate();
        // 선택한 yyyy-MM 에 작성한 회고리스트 조회
        List<Retrospect> getRetrospects = getExistRetrospect(member, selectDate);
        // 선택한 yyyy-MM 에 작성한 회고 중, 수정하고자 하는 회고가 존재하지 않을 경우
        if(getRetrospects.size() < reqEditRetroDto.getWeek()) throw new RetrospectNotFoundException("조회하고자 하는 회고가 존재하지 않습니다.");
        // 몇번째 회고인지
        Retrospect retrospect = getRetrospects.get(reqEditRetroDto.getWeek());
        // 회고에서 어떤 질문에 대한 답을 수정했는지
        List<RetrospectContent> retrospectContents = retrospect.getRetrospectContents();
        // 내용 수정
        retrospectContents.get(reqEditRetroDto.getIndex()).changeAnswer(reqEditRetroDto.getAnswer());
    }


    public RespGetKeywordAndEmotionDto getKeywordAndEmotion(String socialId, ReqGetKeywordAndEmotionDto reqGetKeywordAndEmotionDto){
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> new MemberAuthException("존재하지 않는 유저입니다."));

        LocalDateTime currentTime = reqGetKeywordAndEmotionDto.getCurrentDate();
        LocalDateTime prevRetroDate = currentTime.with(TemporalAdjusters.previousOrSame(member.getRetrospectDay()));

        //일주일 일기 리스트 조회
        List<Diary> diaries = diaryRepository.findListByMemberAndBetweenWriteDate(
                member.getMemberId(),
                prevRetroDate.toLocalDate().minusDays(6),
                currentTime.toLocalDate(),
                true
                );

        RespGetKeywordAndEmotionDto respGetKeywordAndEmotionDto = RespGetKeywordAndEmotionDto.makeRespGetKeywordAndEmotionDto(diaries);

        return respGetKeywordAndEmotionDto;
    }


    public RespGetQuestionAndHelpDto getQuestionAndHelp(ReqGetGoalDto reqGetGoalDto) {
        // 회고 질문 + 도움말 조회
        List<Question> retrospectQuestions = questionRepository.findListByGoal(reqGetGoalDto.getGoalIndex());

        RespGetQuestionAndHelpDto respGetQuestionAndHelpDto = RespGetQuestionAndHelpDto.makeRespGetQuestionAndHelpDto(retrospectQuestions);

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

    private List<RespGetClassifiedKeywordDto> getKeyword(Member member, LocalDateTime selectTime) {
        // 전체 분할한 키워드 리스트들
        // 분류1 : [
        //   1차 : [
        //   ]
        //   2차 : [
        //   ]
        //   3차 : [
        //   ]
        //]

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

        List<ClassifyDto> classifyDtos = new ArrayList<>();
        List<RespGetClassifiedKeywordDto> respGetClassifiedKeywordDtos = new ArrayList<>();
        RespGetClassifiedKeywordDto respGetClassifiedKeywordDto = new RespGetClassifiedKeywordDto();
        for (int i = 0; i < keyWordClass.size(); i++) {
            classifyDtos = new ArrayList<>();
            for (Retrospect t : writeRetrospect) {
                //한 회고에 대한 키워드 리스트
                List<RetrospectKeyword> classifiedKeyword = retrospectKeywordRepository.findListByRetroAndClassify(t.getRetrospectId(), keyWordClass.get(i));
                ClassifyDto classifyDto = new ClassifyDto();
                //t차 회고의 i 번째 분류 과정 시작 가장 첫 시작은 첫번째 회고의 첫번째 분류
                classifyDto = ClassifyDto.makeClassifyDto(classifiedKeyword);
                classifyDtos.add(classifyDto);
            }
            // i 번째 분류 과정 완
            respGetClassifiedKeywordDto = RespGetClassifiedKeywordDto.makeRespGetExistRetrospectKeyword(classifyDtos, keyWordClass.get(i));
            respGetClassifiedKeywordDtos.add(respGetClassifiedKeywordDto);
        }
        return respGetClassifiedKeywordDtos;
    }
}
