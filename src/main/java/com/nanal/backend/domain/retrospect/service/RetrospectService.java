package com.nanal.backend.domain.retrospect.service;

import com.nanal.backend.domain.diary.domain.DiaryWritableWeek;
import com.nanal.backend.domain.diary.entity.Diary;
import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.domain.diary.entity.Emotion;
import com.nanal.backend.domain.diary.entity.Keyword;
import com.nanal.backend.domain.diary.entity.KeywordEmotion;
import com.nanal.backend.domain.diary.repository.EmotionRepository;
import com.nanal.backend.domain.retrospect.dto.req.*;
import com.nanal.backend.domain.retrospect.dto.resp.*;
import com.nanal.backend.domain.retrospect.entity.*;
import com.nanal.backend.domain.retrospect.exception.*;
import com.nanal.backend.domain.retrospect.repository.ExtraQuestionRepository;
import com.nanal.backend.domain.retrospect.repository.QuestionRepository;
import com.nanal.backend.global.exception.customexception.MemberAuthException;
import com.nanal.backend.domain.diary.repository.diary.DiaryRepository;
import com.nanal.backend.domain.auth.repository.MemberRepository;
import com.nanal.backend.domain.retrospect.repository.RetrospectKeywordRepository;
import com.nanal.backend.domain.retrospect.repository.retrospect.RetrospectRepository;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Math.abs;

@Timed("retrospect.api")
@EnableScheduling
@RequiredArgsConstructor
@Transactional
@Service
public class RetrospectService {

    private final MemberRepository memberRepository;
    private final DiaryRepository diaryRepository;
    private final RetrospectRepository retrospectRepository;
    private final RetrospectKeywordRepository retrospectKeywordRepository;
    private final QuestionRepository questionRepository;
    private final ExtraQuestionRepository extraQuestionRepository;
    private final EmotionRepository emotionRepository;

    @Counted("retrospect.api.count")
    @Transactional(readOnly = true)
    public RespGetInfoDto getInfo(String socialId, ReqGetInfoDto reqGetInfoDto) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> MemberAuthException.EXCEPTION);

        RespGetInfoDto info = getRespGetInfoDto(reqGetInfoDto, member);

        return info;
    }
    @Counted("retrospect.api.count")
    public void saveRetrospect(String socialId, ReqSaveRetroDto reqSaveRetroDto) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> MemberAuthException.EXCEPTION);

        //회고 작성 가능성 검증
        checkRetrospectWritable(member, reqSaveRetroDto.getCurrentDate());

        // 회고 Entity 생성
        Retrospect retrospect = Retrospect.createRetrospect(member, reqSaveRetroDto);

        // 회고 저장
        retrospectRepository.save(retrospect);

        //회고 저장 후 일주일 일기 리스트 editstatus 변경
        changeDiaryEditStatus(member, reqSaveRetroDto);
    }
    @Counted("retrospect.api.count")
    @Transactional(readOnly = true)
    public RespGetRetroDto getRetro(String socialId, ReqGetRetroDto reqGetRetroDto) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> MemberAuthException.EXCEPTION);

        //조회할 회고 찾기
        Retrospect selectRetrospect = retrospectRepository.getRetrospect(member.getMemberId(), reqGetRetroDto.getFromDate(),
                reqGetRetroDto.getToDate(), reqGetRetroDto.getWeek());

        // 몇번째 회고인지 조회한 후, 회고 리스트로 반환값 생성
        return RespGetRetroDto.createRespGetRetroDto(selectRetrospect);
    }

    @Counted("retrospect.api.count")
    public void editRetrospect(String socialId, ReqEditRetroDto reqEditRetroDto) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> MemberAuthException.EXCEPTION);
        // 서버 현재 시간
        LocalDateTime currentDate = LocalDateTime.now();

        //회고 수정 가능성 검증
        checkWriteTime(member, currentDate);

        //조회할 회고 찾기
        Retrospect selectRetrospect = retrospectRepository.getRetrospect(member.getMemberId(), currentDate.toLocalDate().atStartOfDay().withDayOfMonth(1),
                currentDate.toLocalDate().atStartOfDay().withDayOfMonth(LocalDate.now().lengthOfMonth()), reqEditRetroDto.getWeek());

        selectRetrospect.changeAnswer(reqEditRetroDto);
    }
    @Counted("retrospect.api.count")
    @Transactional(readOnly = true)
    public RespGetKeywordAndEmotionDto getKeywordAndEmotion(String socialId){

        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> MemberAuthException.EXCEPTION);

        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime prevRetroDate = currentDate.with(TemporalAdjusters.previousOrSame(member.getRetrospectDay()));

        //자정 안에 호출했는지 체크. 자정 안이라면 true, 아니면 false
        boolean isInTime = checkIsInTime(prevRetroDate, currentDate);

        //일주일 일기 리스트 조회
        List<Diary> diaries = diaryRepository.findDiaryListByMemberAndBetweenWriteDate(
                member.getMemberId(),
                prevRetroDate.toLocalDate().minusDays(6),
                currentDate.toLocalDate(),
                true
        );

        //감정어 필터링 이후 count
        List<CountEmotion> countEmotions = getEmotionCount(diaries);

        return RespGetKeywordAndEmotionDto.createRespGetKeywordAndEmotionDto(isInTime, currentDate, diaries, countEmotions);
    }
    @Counted("retrospect.api.count")
    @Transactional(readOnly = true)
    public RespGetQuestionAndHelpDto getQuestionAndHelp(ReqGetGoalDto reqGetGoalDto) {
        long goalIndex = getGoalIndex(reqGetGoalDto.getGoal());
        // 회고 질문 + 도움말 조회
        List<Question> retrospectQuestions = questionRepository.findListByGoal(goalIndex);

        return RespGetQuestionAndHelpDto.createRespGetQuestionAndHelpDto(retrospectQuestions);
    }
    @Counted("retrospect.api.count")
    @Transactional(readOnly = true)
    public RespGetExtraQuestionAndHelpDto getExtraQuestionAndHelp(String socialId, ReqGetGoalDto reqGetGoalDto){
        long goalIndex = getGoalIndex(reqGetGoalDto.getGoal());
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> MemberAuthException.EXCEPTION);
        //유저가 작성한 회고 리스트
        List<String> contents = getContents(member.getMemberId());

        List<ExtraQuestion> selected = getSelectedQuestion(goalIndex, contents);

        return RespGetExtraQuestionAndHelpDto.createRespGetQuestionAndHelpDto(selected);
    }
    @Counted("retrospect.api.count")
    @Transactional(readOnly = true)
    public RespCheckFirstRetrospect checkFirstRetrospect(String socialId) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> MemberAuthException.EXCEPTION);
        //서버 현재 시간
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime prevRetroDate = currentDate.with(TemporalAdjusters.previousOrSame(member.getRetrospectDay()));
        LocalDateTime postRetroDate = DiaryWritableWeek.getRetroDate(member.getRetrospectDay(), member.getPrevRetrospectDate());
        LocalDateTime startDate = currentDate.toLocalDate().atStartOfDay();
        LocalDateTime endDate = currentDate.toLocalDate().atTime(LocalTime.MAX).withNano(0);

        //회고일 변경 후 첫 회고 판별. 첫 회고가 맞다면 true 반환, 아니면 false 반환
        boolean checkfirstRetrospect = checkFirst(postRetroDate, currentDate);
        //회고일에 작성한 일기가 있는지. 있다면 true, 없다면 false
        boolean writtenDiary = diaryRepository.checkWrittenDiary(member.getMemberId(), startDate, endDate);

        //일주일 일기 리스트 count
        int diarycount = countDiary(member.getMemberId(), prevRetroDate, currentDate);

        if (checkfirstRetrospect == true)
            return RespCheckFirstRetrospect.firstRetrospectAfterChange(checkfirstRetrospect, writtenDiary, diarycount);
        else
            return RespCheckFirstRetrospect.notFirstRetrospectAfterChange(checkfirstRetrospect, writtenDiary, diarycount);

    }
    @Counted("retrospect.api.count")
    public void deleteRetro(String socialId, ReqDeleteRetroDto reqDeleteRetroDto) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> MemberAuthException.EXCEPTION);

        // 삭제할 회고 가져오기
        Retrospect deleteRetro = retrospectRepository.getRetrospect(member.getMemberId(), reqDeleteRetroDto.getFromDate(), reqDeleteRetroDto.getToDate(), reqDeleteRetroDto.getWeek());
        // 기존 회고 삭제
        delete(member, deleteRetro);
    }

    //===편의 메서드===//
    private RespGetInfoDto getRespGetInfoDto (ReqGetInfoDto reqGetInfoDto, Member member) {
        LocalDateTime currentDate = LocalDateTime.now();
        // 선택한 월에 있는 회고 기록 ( 어떤 회고 목적을 선택했는가 )
        List<String> existRetrospect = retrospectRepository.getRetrospectGoal(member.getMemberId(), reqGetInfoDto.getFromDate(), reqGetInfoDto.getToDate());
        //회고 개수가 5개인지 5개 아니면 true, 이상이면 false
        boolean isRetroNumberNotFive = retrospectRepository.checkRetroNotOverFive(member.getMemberId(), reqGetInfoDto.getFromDate(), reqGetInfoDto.getToDate());
        // 회고 요일까지 남은 날짜
        LocalDateTime postRetroDate = DiaryWritableWeek.getRetroDate(member.getRetrospectDay(), currentDate);
        Period period = Period.between(currentDate.toLocalDate(), postRetroDate.toLocalDate());
        int betweenDate = getbetweenDate(member, currentDate, period);
        // 회고 주제별로 분류 후 주차별로 분류
        List<RespGetClassifiedKeywordDto> respGetClassifiedKeywordDtos = getKeyword(member, reqGetInfoDto.getFromDate(), reqGetInfoDto.getToDate());

        return RespGetInfoDto.builder()
                .nickname(member.getNickname())
                .existRetrospect(existRetrospect)
                .betweenDate(betweenDate)
                .countRetrospect(isRetroNumberNotFive)
                .keywordList(respGetClassifiedKeywordDtos)
                .build();
    }
    //다음 회고까지 남은 날 반환
    private Integer getbetweenDate(Member member, LocalDateTime currentDate, Period period) {
        int betweenDate = period.getDays();
        if (checkExistRetro(member, currentDate) == true)
            betweenDate = 7;
        return betweenDate;
    }
    //회고 작성 예외처리 메서드 묶음
    private void checkRetrospectWritable(Member member, LocalDateTime currentDate) {
        //작성한 회고가 5개 넘어가는지 여부
        retrospectRepository.checkRetroCount(member.getMemberId(), currentDate.toLocalDate().atStartOfDay().withDayOfMonth(1),
                currentDate.toLocalDate().atStartOfDay().withDayOfMonth(currentDate.toLocalDate().lengthOfMonth()));
        //회고 작성한 시간 체크 (회고 작성은 회고일 당일 11:59 까지만 가능) 1. 요청 들어온 요일이 유저 회고요일과 같은지 체크
        checkWriteTime(member, currentDate);
        // 해당 날짜에 작성한 회고 존재하는지 체크
        retrospectRepository.checkRetrospectAlreadyExist(member.getMemberId(), currentDate);
    }
    //회고 작성 예외처리
    private void checkWriteTime(Member member, LocalDateTime dateTime) {
        //회고 작성한 시간 체크 (회고 작성은 회고일 당일 11:59 까지만 가능) 1. 요청 들어온 요일이 유저 회고요일과 같은지 체크
        DayOfWeek prevDay = dateTime.getDayOfWeek();
        if(prevDay != member.getRetrospectDay())
            throw RetrospectTimeDoneException.EXCEPTION;

        //회고 작성한 시간 체크 (회고 작성은 회고일 당일 11:59 까지만 가능) 2. 요청 들어온 날짜와 회고 날짜가 차이가 1일인지 체크
        LocalDate prevRetroDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(member.getRetrospectDay()));
        if(abs(ChronoUnit.DAYS.between(prevRetroDate, dateTime.toLocalDate())) != 0)
            throw RetrospectTimeDoneException.EXCEPTION;
    }
    private void changeDiaryEditStatus (Member member, ReqSaveRetroDto reqSaveRetroDto) {
        LocalDateTime currentTime = reqSaveRetroDto.getCurrentDate();
        LocalDateTime prevRetroDate = currentTime.with(TemporalAdjusters.previousOrSame(member.getRetrospectDay()));
        List<Diary> diaries = diaryRepository.findDiaryListByMemberAndBetweenWriteDate(member.getMemberId(), prevRetroDate.toLocalDate().minusDays(6), currentTime.toLocalDate(),true);
        for(Diary t : diaries) {
            t.changeEditStatus(false);
        }
    }
    private void changeDiaryEditStatusToTrue (Member member, LocalDateTime prevRetroDate, LocalDateTime currentTime) {
        List<Diary> diaries = diaryRepository.findDiaryListByMemberAndBetweenWriteDate(member.getMemberId(), prevRetroDate.toLocalDate().minusDays(6), currentTime.toLocalDate(),false);
        for(Diary t : diaries) {
            t.changeEditStatus(true);
        }
    }
    //회고 존재 여부 API 사용
    public boolean checkRetrospect(String socialId) {
        //서버 현재 시간
        LocalDateTime currentDate = LocalDateTime.now();
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> MemberAuthException.EXCEPTION);
        return checkExistRetro(member, currentDate);
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
    //자정 전에 호출 여부 체크 메서드
    private boolean checkIsInTime(LocalDateTime prevRetroDate, LocalDateTime currentDate) {
        if(abs(ChronoUnit.DAYS.between(prevRetroDate.toLocalDate(), currentDate)) != 0)
            return false;
        else
            return true;
    }
    //일주일 일기 데이터 가져오기
    private List<Diary> getWeekDiaries (Long memberId, LocalDateTime prevRetroDate, LocalDateTime currentDate) {
        List<Diary> diaries = diaryRepository.findDiaryListByMemberAndBetweenWriteDate(
                memberId,
                prevRetroDate.toLocalDate().minusDays(6),
                currentDate.toLocalDate(),
                true
        );

        return diaries;
    }
    //유저가 작성한 회고 리스트 반환 메서드
    private List<String> getContents(Long memberId) {
        List<Retrospect> getRetrospects = retrospectRepository.findListByMember(memberId);
        List<String> contents = getRetrospects.stream()
                .flatMap(retrospect -> retrospect.getRetrospectContents().stream())
                .map(RetrospectContent::getQuestion)
                .collect(Collectors.toList());
        return contents;
    }
    private List<ExtraQuestion> getSelectedQuestion (Long goalIndex, List<String> contents) {
        // 회고 추가 질문 + 도움말 조회
        List<ExtraQuestion> extraRetrospectQuestions = extraQuestionRepository.findListByGoal(goalIndex);
        //작성한 질문 인덱스 담는 리스트
        List<Integer> windex = IntStream.range(0, extraRetrospectQuestions.size())
                .filter(i -> contents.contains(extraRetrospectQuestions.get(i).getContent()))
                .boxed()
                .collect(Collectors.toList());

        List<ExtraQuestion> selected = new ArrayList<>();
        Random r = new Random();

        int[] a;
        if(extraRetrospectQuestions.size() % 2 != 0 && windex.size() == extraRetrospectQuestions.size() - 1){
            int lastIndex = IntStream.range(0, extraRetrospectQuestions.size())
                    .filter(i -> !windex.contains(i))
                    .findFirst()
                    .orElse(-1);
            a = new int[]{lastIndex, r.nextInt(extraRetrospectQuestions.size())};
        } else {
            Set<Integer> indexSet = new HashSet<>();
            while (indexSet.size() < 2) {
                int index = r.nextInt(extraRetrospectQuestions.size());
                if (!windex.contains(index)) {
                    indexSet.add(index);
                }
            }
            a = indexSet.stream()
                    .mapToInt(Integer::intValue)
                    .toArray();
        }

        for (int i : a){
            selected.add(extraRetrospectQuestions.get(i));
        }
        return selected;
    }
    // 첫번째 회고인지 파악 메서드
    private boolean checkFirst (LocalDateTime postRetroDate, LocalDateTime currentDate) {
        if(abs(ChronoUnit.DAYS.between(postRetroDate.toLocalDate(),  currentDate)) == 0)
            return true;
        else
            return false;
    }
    private Integer countDiary(Long memberId, LocalDateTime prevRetroDate, LocalDateTime currentDate) {
        List<Diary> diaries = getWeekDiaries(memberId, prevRetroDate, currentDate);
        return diaries.size();
    }
    private List<RespGetClassifiedKeywordDto> getKeyword(Member member, LocalDateTime fromDate, LocalDateTime toDate) {
        List<Retrospect> writeRetrospect = retrospectRepository.findRetrospectListByMemberAndWriteDate(
                member.getMemberId(),
                fromDate, toDate);

        //회고의 분류 리스트 생성
        List<String> keyWordClass = new ArrayList<>();
        keyWordClass.add("그때 그대로 의미있었던 행복한 기억");
        keyWordClass.add("나를 힘들게 했지만 도움이 된 기억");
        keyWordClass.add("돌아보니, 다른 의미로 다가온 기억");

        List<RespGetClassifiedKeywordDto> respGetClassifiedKeywordDtos = keyWordClass.stream()
                .map(classify -> {
                    List<ClassifyDto> classifyDtos = writeRetrospect.stream()
                            .map(t -> {
                                List<RetrospectKeyword> classifiedKeyword = retrospectKeywordRepository.findListByRetroAndClassify(t.getRetrospectId(), classify);
                                return ClassifyDto.makeClassifyDto(classifiedKeyword);
                            })
                            .collect(Collectors.toList());
                    for (int j = writeRetrospect.size(); j < 5; j++) {
                        classifyDtos.add(new ClassifyDto());
                    }
                    return RespGetClassifiedKeywordDto.makeRespGetExistRetrospectKeyword(classifyDtos, classify);
                })
                .collect(Collectors.toList());
        return respGetClassifiedKeywordDtos;
    }
    private List<CountEmotion> getEmotionCount(List<Diary> diaries) {
        List<String> emotions = diaries.stream()
                .flatMap(d -> d.getKeywords().stream())
                .flatMap(k -> k.getKeywordEmotions().stream())
                .map(ke -> ke.getEmotion().getEmotion())
                .collect(Collectors.toList());

        List<Emotion> findEmotions = emotionRepository.findAll();
        List<CountEmotion> countEmotions = findEmotions.stream()
                .map(e -> {
                    int count = Collections.frequency(emotions, e.getEmotion());
                    int frequency;
                    if (count == 0)
                        frequency = 0;
                    else if (count >= 1 && count <= 5)
                        frequency = 1;
                    else if (count >= 6 && count <= 10)
                        frequency = 2;
                    else
                        frequency = 3;
                    return CountEmotion.makeCountEmotion(e.getEmotion(), frequency);
                })
                .collect(Collectors.toList());
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
    private void delete(Member member, Retrospect retrospect) {
        //서버 현재 시간
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDate currentDate = currentTime.toLocalDate();
        //회고 당일날에 회고 삭제
        if(retrospect.getWriteDate().toLocalDate().compareTo(currentDate) == 0) {
            retrospectRepository.delete(retrospect);
            LocalDateTime prevRetroDate = currentTime.with(TemporalAdjusters.previousOrSame(member.getRetrospectDay()));
            changeDiaryEditStatusToTrue(member, prevRetroDate, currentTime);
        }
        else
            retrospectRepository.delete(retrospect);
    }

}
