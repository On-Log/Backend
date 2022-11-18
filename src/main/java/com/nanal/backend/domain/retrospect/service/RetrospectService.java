package com.nanal.backend.domain.retrospect.service;

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
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new RuntimeException());
        LocalDateTime currentDate = reqGetInfoDto.getCurrentDate();
        LocalDateTime selectDate = reqGetInfoDto.getSelectDate();

        // 선택한 월에 있는 회고 기록 ( 어떤 회고 목적을 선택했는가 )
        List<Retrospect> getretrospects = getExistRetrospect(member, selectDate);
        List<String> existRetrospect = new ArrayList<>();
        for (Retrospect t : getretrospects) {
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

    //===편의 메서드===//
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
        List<Retrospect> writeretrospect = retrospectRepository.findListByMemberAndWriteDate(
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
            for (Retrospect t : writeretrospect) {
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

}
