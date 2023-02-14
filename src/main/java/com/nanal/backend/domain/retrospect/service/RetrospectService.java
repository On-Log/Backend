package com.nanal.backend.domain.retrospect.service;

import com.nanal.backend.domain.diary.entity.Diary;
import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.domain.diary.entity.Emotion;
import com.nanal.backend.domain.diary.entity.Keyword;
import com.nanal.backend.domain.diary.entity.KeywordEmotion;
import com.nanal.backend.domain.diary.repository.EmotionRepository;
import com.nanal.backend.domain.retrospect.dto.req.*;
import com.nanal.backend.domain.retrospect.dto.resp.*;
import com.nanal.backend.domain.retrospect.entity.*;
import com.nanal.backend.domain.retrospect.exception.RetrospectAllDoneException;
import com.nanal.backend.domain.retrospect.exception.RetrospectAlreadyExistException;
import com.nanal.backend.domain.retrospect.exception.RetrospectTimeDoneException;
import com.nanal.backend.domain.retrospect.repository.ExtraQuestionRepository;
import com.nanal.backend.domain.retrospect.repository.QuestionRepository;
import com.nanal.backend.global.exception.customexception.MemberAuthException;
import com.nanal.backend.domain.diary.repository.DiaryRepository;
import com.nanal.backend.domain.diary.service.DiaryService;
import com.nanal.backend.domain.auth.repository.MemberRepository;
import com.nanal.backend.domain.retrospect.repository.RetrospectKeywordRepository;
import com.nanal.backend.domain.retrospect.repository.RetrospectRepository;
import com.nanal.backend.domain.retrospect.exception.RetrospectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.nanal.backend.domain.retrospect.dto.resp.RespGetInfoDto.makeRespGetInfoDto;
import static java.lang.Math.abs;

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
    private final ExtraQuestionRepository extraQuestionRepository;
    private final EmotionRepository emotionRepository;

    public RespGetInfoDto getInfo(String socialId, ReqGetInfoDto reqGetInfoDto) {
        //회고 개수가 5개인지 5개 아니면 true, 이상이면 false
        boolean isRetroNumberNotFive = true;
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> MemberAuthException.EXCEPTION);
        LocalDateTime currentDate = reqGetInfoDto.getCurrentDate();
        LocalDateTime selectDate = reqGetInfoDto.getSelectDate();

        // 선택한 월에 있는 회고 기록 ( 어떤 회고 목적을 선택했는가 )
        List<Retrospect> getRetrospects = getExistRetrospect(member, selectDate);
        List<String> existRetrospect = new ArrayList<>();
        for (Retrospect t : getRetrospects) {
            existRetrospect.add(t.getGoal());
        }
        isRetroNumberNotFive = countRetro(member, reqGetInfoDto.getSelectDate());


        LocalDateTime postRetroDate = diaryService.getRetroDate(member.getRetrospectDay(), currentDate);
        // 회고 요일까지 남은 날짜
        Period period = Period.between(currentDate.toLocalDate(), postRetroDate.toLocalDate());
        int betweenDate = period.getDays();
        if (checkExistRetro(member, currentDate) == true)
            betweenDate = 7;

        // 회고 주제별로 분류 후 주차별로 분류
        List<RespGetClassifiedKeywordDto> respGetClassifiedKeywordDtos = getKeyword(member, selectDate);

        RespGetInfoDto respGetInfoDto = makeRespGetInfoDto(member.getNickname(),existRetrospect, betweenDate, isRetroNumberNotFive, respGetClassifiedKeywordDtos);

        return respGetInfoDto;
    }


    public void saveRetrospect(String socialId, ReqSaveRetroDto reqSaveRetroDto) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> MemberAuthException.EXCEPTION);
        //작성한 회고가 5개 넘어가는지 여부
        if(countRetro(member, reqSaveRetroDto.getCurrentDate()) == false)
            throw RetrospectAllDoneException.EXCEPTION;
        //회고 작성한 시간 체크 (회고 작성은 회고일 당일 11:59 까지만 가능) 1. 요청 들어온 요일이 유저 회고요일과 같은지 체크
        DayOfWeek prevDay = reqSaveRetroDto.getCurrentDate().getDayOfWeek();
        if(prevDay != member.getRetrospectDay())
            throw RetrospectTimeDoneException.EXCEPTION;
        //회고 작성한 시간 체크 (회고 작성은 회고일 당일 11:59 까지만 가능) 2. 요청 들어온 날짜와 회고 날짜가 차이가 1일인지 체크
        LocalDateTime currentTime = reqSaveRetroDto.getCurrentDate();
        LocalDateTime prevRetroDate = currentTime.with(TemporalAdjusters.previousOrSame(member.getRetrospectDay()));
        if(abs(ChronoUnit.DAYS.between(prevRetroDate.toLocalDate(),  LocalDate.now())) != 0)
            throw RetrospectTimeDoneException.EXCEPTION;
        // 해당 날짜에 작성한 일기 존재하는지 체크
        checkRetrospectAlreadyExist(reqSaveRetroDto, member);
        // 회고 Entity 생성
        Retrospect retrospect = createRetrospect(member, reqSaveRetroDto.getGoal(), reqSaveRetroDto.getCurrentDate(), reqSaveRetroDto.getKeywords(), reqSaveRetroDto.getContents());
        // 회고 저장
        retrospectRepository.save(retrospect);
        //회고 저장 후 일주일 일기 리스트 editstatus 변경
        List<Diary> diaries = diaryRepository.findListByMemberAndBetweenWriteDate(member.getMemberId(), prevRetroDate.toLocalDate().minusDays(6), currentTime.toLocalDate(),true);
        for(Diary t : diaries) {
            t.changeEditStatus(false);
        }
    }


    public RespGetRetroDto getRetro(String socialId, ReqGetRetroDto reqGetRetroDto) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> MemberAuthException.EXCEPTION);

        LocalDateTime selectDate = reqGetRetroDto.getSelectDate();
        // 선택한 yyyy-MM 에 작성한 회고리스트 조회
        List<Retrospect> getRetrospects = getExistRetrospect(member, selectDate);
        // 선택한 yyyy-MM 에 작성한 회고 중, 조회하고자 하는 회고가 존재하지 않을 경우
        if(getRetrospects.size() < reqGetRetroDto.getWeek()) throw RetrospectNotFoundException.EXCEPTION;

        // 몇번째 회고인지 조회한 후, 회고 리스트로 반환값 생성
        RespGetRetroDto respGetRetroDto = RespGetRetroDto.makeRespGetRetroDto(getRetrospects.get(reqGetRetroDto.getWeek()));
        return respGetRetroDto;
    }


    public void editRetrospect(String socialId, ReqEditRetroDto reqEditRetroDto) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> MemberAuthException.EXCEPTION);
        LocalDateTime selectDate = reqEditRetroDto.getEditDate();
        //회고 작성한 시간 체크 (회고 작성은 회고일 당일 11:59 까지만 가능)
        DayOfWeek prevDay = selectDate.getDayOfWeek();
        if(prevDay != member.getRetrospectDay())
            throw RetrospectTimeDoneException.EXCEPTION;

        //회고 작성한 시간 체크 (회고 작성은 회고일 당일 11:59 까지만 가능) 2. 요청 들어온 날짜와 회고 날짜가 차이가 1일인지 체크
        LocalDateTime currentTime = reqEditRetroDto.getEditDate();
        LocalDateTime prevRetroDate = currentTime.with(TemporalAdjusters.previousOrSame(member.getRetrospectDay()));
        if(abs(ChronoUnit.DAYS.between(prevRetroDate.toLocalDate(),  LocalDate.now())) != 0)
            throw RetrospectTimeDoneException.EXCEPTION;

        // 선택한 yyyy-MM 에 작성한 회고리스트 조회
        List<Retrospect> getRetrospects = getExistRetrospect(member, selectDate);
        // 선택한 yyyy-MM 에 작성한 회고 중, 수정하고자 하는 회고가 존재하지 않을 경우
        if(getRetrospects.size() < reqEditRetroDto.getWeek()) throw RetrospectNotFoundException.EXCEPTION;
        // 몇번째 회고인지
        Retrospect retrospect = getRetrospects.get(reqEditRetroDto.getWeek());
        // 회고에서 어떤 질문에 대한 답을 수정했는지
        List<RetrospectContent> retrospectContents = retrospect.getRetrospectContents();
        // 내용 수정
        retrospectContents.get(reqEditRetroDto.getIndex()).changeAnswer(reqEditRetroDto.getAnswer());
    }


    public RespGetKeywordAndEmotionDto getKeywordAndEmotion(String socialId, ReqGetKeywordAndEmotionDto reqGetKeywordAndEmotionDto){
        //자정 안에 호출했는지 체크. 자정 안이라면 true, 아니면 false
        boolean isInTime = true;
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> MemberAuthException.EXCEPTION);

        LocalDateTime currentTime = reqGetKeywordAndEmotionDto.getCurrentDate();
        LocalDateTime prevRetroDate = currentTime.with(TemporalAdjusters.previousOrSame(member.getRetrospectDay()));
        if(abs(ChronoUnit.DAYS.between(prevRetroDate.toLocalDate(), currentTime)) != 0)
            isInTime = false;


        //일주일 일기 리스트 조회
        List<Diary> diaries = diaryRepository.findListByMemberAndBetweenWriteDate(
                member.getMemberId(),
                prevRetroDate.toLocalDate().minusDays(6),
                currentTime.toLocalDate(),
                true
        );

        //감정어 필터링 이후 count
        List<CountEmotion> countEmotions = getEmotionCount(diaries);

        RespGetKeywordAndEmotionDto respGetKeywordAndEmotionDto = RespGetKeywordAndEmotionDto.makeRespGetKeywordAndEmotionDto(isInTime, currentTime, diaries, countEmotions);

        return respGetKeywordAndEmotionDto;
    }


    public RespGetQuestionAndHelpDto getQuestionAndHelp(ReqGetGoalDto reqGetGoalDto) {
        long goalIndex = getGoalIndex(reqGetGoalDto.getGoal());
        // 회고 질문 + 도움말 조회
        List<Question> retrospectQuestions = questionRepository.findListByGoal(goalIndex);

        RespGetQuestionAndHelpDto respGetQuestionAndHelpDto = RespGetQuestionAndHelpDto.makeRespGetQuestionAndHelpDto(retrospectQuestions);

        return respGetQuestionAndHelpDto;
    }

    public RespGetExtraQuestionAndHelpDto getExtraQuestionAndHelp(String socialId, ReqGetGoalDto reqGetGoalDto){
        long goalIndex = getGoalIndex(reqGetGoalDto.getGoal());
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> MemberAuthException.EXCEPTION);
        //유저가 작성한 회고 리스트
        List<Retrospect> getRetrospects = retrospectRepository.findListByMember(member.getMemberId());
        List<String> contents = new ArrayList<>();
        for (Retrospect retrospect : getRetrospects) {
            List<RetrospectContent> content = new ArrayList<>();
            content = retrospect.getRetrospectContents();
            for(RetrospectContent r : content) {
                contents.add(r.getQuestion());
            }
        }
        // 회고 추가 질문 + 도움말 조회
        List<ExtraQuestion> extraRetrospectQuestions = extraQuestionRepository.findListByGoal(goalIndex);
        //작성한 질문 인덱스 담는 리스트
        ArrayList<Integer> windex = new ArrayList<>();
        //아직 모든 질문에 대한 답을 안했을 때
        for (int i = 0; i < extraRetrospectQuestions.size(); i++) {
            if (contents.contains(extraRetrospectQuestions.get(i).getContent()) == true) {
                windex.add(i);
            }
            if(windex.size() == extraRetrospectQuestions.size()){
                windex = new ArrayList<>();
            }
        }
        List<ExtraQuestion> selected = new ArrayList<>();
        //중복없는 랜덤 숫자
        int a[] = new int[2];
        Random r = new Random();

        //데이터 개수 홀수일 때
        if(extraRetrospectQuestions.size() % 2 != 0 && windex.size() == extraRetrospectQuestions.size() - 1){
            int lastIndex = 0;
            for(int i = 0; i < extraRetrospectQuestions.size(); i++){
                if(windex.contains(i) == false){
                    lastIndex = i;
                    break;
                }
            }
            a[0] = lastIndex;
            while(true){
                a[1] = r.nextInt(extraRetrospectQuestions.size());
                if(a[0] != a[1])
                    break;
            }
        }
        else {
            System.out.println("아직 모든 질문에 대한 답을 안했을 때");
            for (int i = 0; i < 2; i++) {
                System.out.println(i);
                a[i] = r.nextInt(extraRetrospectQuestions.size());
                if (windex.contains(a[i]) == true) {
                    i--;
                    continue;
                }
                for (int j = 0; j < i; j++) {
                    if (a[i] == a[j])
                        i--;
                }
            }
        }

        for (int i = 0; i < 2; i++){
            selected.add(extraRetrospectQuestions.get(a[i]));
        }


        RespGetExtraQuestionAndHelpDto respGetExtraQuestionAndHelpDto = RespGetExtraQuestionAndHelpDto.makeRespGetQuestionAndHelpDto(selected);

        return respGetExtraQuestionAndHelpDto;
    }

    public RespCheckFirstRetrospect checkFirstRetrospect(String socialId, ReqCheckRetroDto reqCheckRetroDto) {
        //회고일 변경 후 첫 회고 판별. 첫 회고가 맞다면 true 반환, 아니면 false 반환
        boolean checkfirstRetrospect = false;
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> MemberAuthException.EXCEPTION);

        LocalDateTime postRetroDate = diaryService.getRetroDate(member.getRetrospectDay(), member.getPrevRetrospectDate());
        System.out.println(postRetroDate);

        if(abs(ChronoUnit.DAYS.between(postRetroDate.toLocalDate(),  reqCheckRetroDto.getCurrentDate())) == 0)
            checkfirstRetrospect = true;

        if (checkfirstRetrospect == true)
            return RespCheckFirstRetrospect.firstRetrospectAfterChange(checkfirstRetrospect);
        else
            return RespCheckFirstRetrospect.notFirstRetrospectAfterChange(checkfirstRetrospect);

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

    //회고 존재 여부 API 사용
    public boolean checkRetrospect(String socialId, ReqCheckRetroDto reqCheckRetroDto) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> MemberAuthException.EXCEPTION);
        return checkExistRetro(member, reqCheckRetroDto.getCurrentDate());
    }

    //회고 저장시 예외처리
    private void checkRetrospectAlreadyExist(ReqSaveRetroDto reqSaveRetroDto, Member member) {
        // 질의할 sql 의 Like 절에 해당하게끔 변환
        String yearMonthDay = reqSaveRetroDto.getCurrentDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "%";
        // 선택한 yyyy-MM-dd 에 작성한 회고 조회
        List<Retrospect> existRetrospect = retrospectRepository.findListByMemberAndWriteDate(member.getMemberId(), yearMonthDay);

        if(existRetrospect.size() != 0) throw RetrospectAlreadyExistException.EXCEPTION;
    }

    //회고 메인탭 회고 체크 편의 메서드
    private boolean checkExistRetro(Member member, LocalDateTime currentDate) {
        String yearMonthDay = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "%";
        // 선택한 yyyy-MM-dd 에 작성한 회고 조회
        List<Retrospect> existRetrospect = retrospectRepository.findListByMemberAndWriteDate(member.getMemberId(), yearMonthDay);
        //회고가 이미 존재하면 true 리턴
        if(existRetrospect.size() != 0)
            return true;
        else
            return false;
    }
    //회고 개수 count
    private boolean countRetro(Member member, LocalDateTime dateTime) {
        int count = 0;
        List<Retrospect> getRetrospects = getExistRetrospect(member, dateTime);
        for (Retrospect t : getRetrospects) {
            count++;
        }
        if (count >= 5)
            return false;
        else
            return true;
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
            for(int j = 0; j < (5-writeRetrospect.size()); j++){
                ClassifyDto classifyDto = new ClassifyDto();
                classifyDtos.add(classifyDto);
            }
            // i 번째 분류 과정 완
            respGetClassifiedKeywordDto = RespGetClassifiedKeywordDto.makeRespGetExistRetrospectKeyword(classifyDtos, keyWordClass.get(i));
            respGetClassifiedKeywordDtos.add(respGetClassifiedKeywordDto);
        }
        return respGetClassifiedKeywordDtos;
    }

    private List<CountEmotion> getEmotionCount(List<Diary> diaries) {
        List<CountEmotion> countEmotions = new ArrayList<>();
        List<String> emotions = new ArrayList<>();
        for( Diary d : diaries ) {
            d.getKeywords();
            for(Keyword k : d.getKeywords()) {
                k.getKeywordEmotions();
                for(KeywordEmotion ke : k.getKeywordEmotions()) {
                    emotions.add(ke.getEmotion().getEmotion());
                }
            }
        }
        List<Emotion> findEmotions = emotionRepository.findAll();
        for( Emotion e : findEmotions ) {
            int frequency = 0;
            int count = Collections.frequency(emotions, e.getEmotion());
            if (count == 0)
                frequency = 0;
            else if (count >= 1 && count <= 10)
                frequency = 1;
            else if (count >= 11 && count <= 20)
                frequency = 2;
            else
                frequency = 3;
            CountEmotion countEmotion = CountEmotion.makeCountEmotion(e.getEmotion(), frequency);
            countEmotions.add(countEmotion);
        }
        return countEmotions;
    }

    private long getGoalIndex(String goal) {
        if (goal.equals("자아탐색"))
            return 1;
        else if (goal.equals("성취확인"))
            return 2;
        else if (goal.equals("감정정리"))
            return 3;
        else if (goal.equals("관계고민"))
            return 4;
        else throw GoalNotFoundException.EXCEPTION;
    }
}
