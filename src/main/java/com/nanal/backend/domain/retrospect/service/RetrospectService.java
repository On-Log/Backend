package com.nanal.backend.domain.retrospect.service;

import com.nanal.backend.domain.diary.domain.DiaryWritableWeek;
import com.nanal.backend.domain.diary.entity.Diary;
import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.domain.diary.entity.Emotion;
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
    private static final int FREQUENCY_HIGH = 3;
    private static final int FREQUENCY_MEDIUM = 2;
    private static final int FREQUENCY_LOW = 1;

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
        checkRetrospectWritable(member, reqSaveRetroDto.getCurrentDate(), reqSaveRetroDto.getContents());

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

    // 기존 회고 조회를 이 API와 통합할 예정
    @Counted("retrospect.api.count")
    public RespGetSearchRetroDto getRetroBySearch(ReqSearchRetroDto reqSearchRetroDto) {
        //조회할 회고 찾기
        Retrospect selectRetrospect = retrospectRepository.findById(reqSearchRetroDto.getRetrospectId()).orElseThrow(() -> RetrospectNotFoundException.EXCEPTION);
        // 몇 주차인지 계산
        Integer week = countWeek(selectRetrospect.getWriteDate());
        // 몇번째 회고인지 조회한 후, 회고 리스트로 반환값 생성
        return RespGetSearchRetroDto.createRespGetSearchRetroDto(selectRetrospect, week);
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
    public RespGetKeywordAndEmotionDto getKeywordAndEmotion(String socialId){

        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> MemberAuthException.EXCEPTION);

        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime prevRetroDate = currentDate.with(TemporalAdjusters.previousOrSame(member.getRetrospectDay()));

        //자정 안에 호출했는지 체크. 자정 안이라면 true, 아니면 false
        boolean isInTime = checkByTime(prevRetroDate, currentDate);

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
        boolean checkfirstRetrospect = checkByTime(postRetroDate, currentDate);
        //회고일에 작성한 일기가 있는지. 있다면 true, 없다면 false
        boolean writtenDiary = diaryRepository.checkWrittenDiary(member.getMemberId(), startDate, endDate);

        //일주일 일기 리스트 count
        int diarycount = diaryRepository.findDiaryListByMemberAndBetweenWriteDate(
                member.getMemberId(),
                prevRetroDate.toLocalDate().minusDays(6),
                currentDate.toLocalDate(),
                true
        ).size();

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
        // 선택한 월에 있는 회고 기록 ( 어떤 회고 목적을 선택했는가, 회고 Id )
        List<Retrospect> existRetrospect = retrospectRepository.findRetrospectListByMemberAndWriteDate(member.getMemberId(), reqGetInfoDto.getFromDate(), reqGetInfoDto.getToDate());
        List<String> retrospectGoal = getRetrospectGoal(existRetrospect);
        List<Long> retrospectId = getRetrospectId(existRetrospect);
        //회고 개수가 5개인지 5개 아니면 true, 이상이면 false
        boolean isRetroNumberNotFive = retrospectRepository.checkRetroNotOverFive(member.getMemberId(), reqGetInfoDto.getFromDate(), reqGetInfoDto.getToDate());
        // 회고 요일까지 남은 날짜
        LocalDateTime postRetroDate = DiaryWritableWeek.getRetroDate(member.getRetrospectDay(), currentDate);
        Period period = Period.between(currentDate.toLocalDate(), postRetroDate.toLocalDate());
        int betweenDate = getbetweenDate(member, currentDate, period);
        // 회고 주제별로 분류 후 주차별로 분류
        List<RespGetClassifiedKeywordDto> respGetClassifiedKeywordDtos = getKeyword(member, reqGetInfoDto.getFromDate(), reqGetInfoDto.getToDate());

        return RespGetInfoDto.createRespGetInfoDto(member.getNickname(),retrospectGoal, retrospectId, betweenDate, isRetroNumberNotFive, respGetClassifiedKeywordDtos);
    }
    // 회고 주차 반환
    private Integer countWeek(LocalDateTime writeDate) {
        LocalDateTime startOfMonth = writeDate.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        int week = retrospectRepository.getWeekSequence(writeDate,startOfMonth);
        return week;
    }
    //다음 회고까지 남은 날 반환
    private Integer getbetweenDate(Member member, LocalDateTime currentDate, Period period) {
        return checkExistRetro(member, currentDate) ? 7 : period.getDays();
    }
    //회고 작성 예외처리 메서드 묶음
    private void checkRetrospectWritable(Member member, LocalDateTime currentDate, List<RetrospectContentDto> contents) {
        //작성한 회고가 5개 넘어가는지 여부
        retrospectRepository.checkRetroCount(member.getMemberId(), currentDate.toLocalDate().atStartOfDay().withDayOfMonth(1),
                currentDate.toLocalDate().atStartOfDay().withDayOfMonth(currentDate.toLocalDate().lengthOfMonth()));
        //회고 작성한 시간 체크 (회고 작성은 회고일 당일 11:59 까지만 가능) 1. 요청 들어온 요일이 유저 회고요일과 같은지 체크
        checkWriteTime(member, currentDate);
        // 해당 날짜에 작성한 회고 존재하는지 체크
        retrospectRepository.checkRetrospectAlreadyExist(member.getMemberId(), currentDate);
        // 회고 답변 개수가 3개 이상, 5개 이하인지 체크
        if(contents.size() < 3 || contents.size() > 5)
            throw WrongContentSizeException.EXCEPTION;
    }
    //회고 작성 예외처리
    private void checkWriteTime(Member member, LocalDateTime dateTime) {
        // 요일과 날짜가 모두 일치하는지 체크
        LocalDate prevRetroDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(member.getRetrospectDay()));
        if (!prevRetroDate.equals(dateTime.toLocalDate()))
            throw RetrospectTimeDoneException.EXCEPTION;
    }

    private void changeDiaryEditStatus (Member member, ReqSaveRetroDto reqSaveRetroDto) {
         LocalDateTime prevRetroDate = reqSaveRetroDto.getCurrentDate().with(TemporalAdjusters.previousOrSame(member.getRetrospectDay()));
        diaryRepository.findDiaryListByMemberAndBetweenWriteDate(member.getMemberId(), prevRetroDate.toLocalDate().minusDays(6), reqSaveRetroDto.getCurrentDate().toLocalDate(), true)
                .forEach(diary -> diary.changeEditStatus(false));
    }
    private void changeDiaryEditStatusToTrue (Member member, LocalDateTime prevRetroDate, LocalDateTime currentTime) {
        diaryRepository.findDiaryListByMemberAndBetweenWriteDate(member.getMemberId(), prevRetroDate.toLocalDate().minusDays(6), currentTime.toLocalDate(), false)
                .forEach(d -> d.changeEditStatus(true));
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
        return retrospectRepository.findListByMemberAndWriteDate(member.getMemberId(), yearMonthDay).size() != 0;
    }
    //자정 전에 호출 여부 체크, 첫번째 회고인지 파악 하는 통합 메서드
    private boolean checkByTime(LocalDateTime Date, LocalDateTime currentDate) {
        return abs(ChronoUnit.DAYS.between(Date.toLocalDate(), currentDate)) == 0;
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
        List<ExtraQuestion> extraRetrospectQuestions = extraQuestionRepository.findListByGoal(goalIndex);
        List<ExtraQuestion> unselectedQuestions = extraRetrospectQuestions.stream()
                .filter(question -> !contents.contains(question.getContent()))
                .collect(Collectors.toList());

        if (unselectedQuestions.size() == 0) {
            unselectedQuestions.addAll(extraRetrospectQuestions);
        }

        List<Integer> indexes = new ArrayList<>();
        while (indexes.size() < 2 && unselectedQuestions.size() > 0) {
            int randomIndex = new Random().nextInt(unselectedQuestions.size());
            int realIndex = extraRetrospectQuestions.indexOf(unselectedQuestions.get(randomIndex));
            if (!indexes.contains(realIndex)) {
                indexes.add(realIndex);
            }
        }

        List<ExtraQuestion> selectedQuestions = indexes.stream()
                .map(extraRetrospectQuestions::get)
                .collect(Collectors.toList());

        return selectedQuestions;
    }
    private List<RespGetClassifiedKeywordDto> getKeyword(Member member, LocalDateTime fromDate, LocalDateTime toDate) {
        List<String> keyWordClass = List.of(
                "그때 그대로 의미있었던 행복한 기억",
                "나를 힘들게 했지만 도움이 된 기억",
                "돌아보니, 다른 의미로 다가온 기억"
        );

        return keyWordClass.stream()
                .map(classify -> {
                    List<ClassifyDto> classifyDtos = retrospectRepository.findRetrospectListByMemberAndWriteDate(
                                    member.getMemberId(),
                                    fromDate, toDate
                            ).stream()
                            .map(t -> ClassifyDto.makeClassifyDto(
                                    retrospectKeywordRepository.findListByRetroAndClassify(
                                            t.getRetrospectId(), classify
                                    )
                            ))
                            .collect(Collectors.toList());

                    classifyDtos.addAll(Collections.nCopies(5 - classifyDtos.size(), new ClassifyDto()));
                    return RespGetClassifiedKeywordDto.makeRespGetExistRetrospectKeyword(classifyDtos, classify);
                })
                .collect(Collectors.toList());
    }
    private List<CountEmotion> getEmotionCount(List<Diary> diaries) {
        Set<Integer> set = new HashSet<>();
        List<Emotion> emotions = emotionRepository.findAll();
        List<CountEmotion> result = new ArrayList<>();
        Map<Emotion, Integer> emotionCountMap = new LinkedHashMap<>(); // 순서를 보장하는 LinkedHashMap 사용

        for (Emotion e : emotions) {
            int count = diaries.stream()
                    .flatMap(diary -> diary.getKeywords().stream())
                    .flatMap(keyword -> keyword.getKeywordEmotions().stream())
                    .filter(ke -> ke.getEmotion().getEmotion().equals(e.getEmotion()))
                    .mapToInt(ke -> 1)
                    .sum();

            set.add(count);
            emotionCountMap.put(e, count); // 감정어 순서대로 저장
        }
        set.remove(0);
        if (set.size() == 1) {
            Integer value = set.iterator().next();

            for (Emotion emotion : emotions) {
                Integer count = emotionCountMap.get(emotion);
                int frequency = count.equals(value) ? FREQUENCY_HIGH : 0;

                CountEmotion countEmotion = CountEmotion.makeCountEmotion(emotion.getEmotion(), frequency);
                result.add(countEmotion);
            }
        } else if (set.size() == 2) {
            List<Integer> sortedList = new ArrayList<>(set);
            Collections.sort(sortedList, Collections.reverseOrder());
                for (Emotion emotion : emotions) {
                    Integer count = emotionCountMap.get(emotion);
                    int frequency = 0;

                    if (count.equals(sortedList.get(0))) {
                        frequency = FREQUENCY_HIGH;
                    } else if (count.equals(sortedList.get(1))) {
                        frequency = FREQUENCY_MEDIUM;
                    }

                    CountEmotion countEmotion = CountEmotion.makeCountEmotion(emotion.getEmotion(), frequency);
                    result.add(countEmotion);
                }
        } else if (set.size() == 3) {
            List<Integer> sortedList = new ArrayList<>(set);
            Collections.sort(sortedList, Collections.reverseOrder());
                for (Emotion emotion : emotions) {
                    Integer count = emotionCountMap.get(emotion);
                    int frequency = 0;

                    if (count.equals(sortedList.get(0))) {
                        frequency = FREQUENCY_HIGH;
                    } else if (count.equals(sortedList.get(1))) {
                        frequency = FREQUENCY_MEDIUM;
                    } else if (count.equals(sortedList.get(2))) {
                        frequency = FREQUENCY_LOW;
                    }

                    CountEmotion countEmotion = CountEmotion.makeCountEmotion(emotion.getEmotion(), frequency);
                    result.add(countEmotion);
                }
        }else {
            List<Integer> sortedList = new ArrayList<>(set);
            Collections.sort(sortedList, Collections.reverseOrder());

            int size = sortedList.size();
            int fifteenPercentIndex = (int) (size * 0.15);
            int fortyPercentIndex = (int) (size * 0.55);

            for (Emotion emotion : emotions) {
                Integer count = emotionCountMap.get(emotion);
                int frequency = 0;

                if (count == null) {
                    continue;
                }

                int index = sortedList.indexOf(count);
                if (count == 0) {
                    frequency = 0;
                } else if (index <= fifteenPercentIndex) {
                    frequency = FREQUENCY_HIGH;
                } else if (index <= fortyPercentIndex) {
                    frequency = FREQUENCY_MEDIUM;
                } else {
                    frequency = FREQUENCY_LOW;
                }

                CountEmotion countEmotion = CountEmotion.makeCountEmotion(emotion.getEmotion(), frequency);
                result.add(countEmotion);
            }
        }

        return result;
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
        LocalDate currentDate = LocalDate.now();
        LocalDate retrospectDate = retrospect.getWriteDate().toLocalDate();
        if (retrospectDate.isEqual(currentDate)) {
            retrospectRepository.delete(retrospect);
            LocalDateTime prevRetroDate = LocalDateTime.now().with(TemporalAdjusters.previousOrSame(member.getRetrospectDay()));
            changeDiaryEditStatusToTrue(member, prevRetroDate, LocalDateTime.now());
        } else {
            retrospectRepository.delete(retrospect);
        }
    }

    private List<String> getRetrospectGoal(List<Retrospect> retrospects) {
        return retrospects.stream()
                .map(Retrospect::getGoal)
                .collect(Collectors.toList());
    }

    private List<Long> getRetrospectId(List<Retrospect> retrospects) {
        return retrospects.stream()
                .map(Retrospect::getRetrospectId)
                .collect(Collectors.toList());
    }
}