package com.nanal.backend.domain.retrospect.v2.service;

import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.domain.auth.repository.MemberRepository;
import com.nanal.backend.domain.diary.domain.DiaryWritableWeek;
import com.nanal.backend.domain.diary.repository.diary.DiaryRepository;
import com.nanal.backend.domain.retrospect.v2.dto.req.ReqDeleteRetroDto;
import com.nanal.backend.domain.retrospect.v2.dto.req.ReqEditRetroDto;
import com.nanal.backend.domain.retrospect.dto.req.ReqGetInfoDto;
import com.nanal.backend.domain.retrospect.dto.resp.ClassifyDto;
import com.nanal.backend.domain.retrospect.dto.resp.RespGetClassifiedKeywordDto;
import com.nanal.backend.domain.retrospect.v2.dto.resp.RespGetRetroDto;
import com.nanal.backend.domain.retrospect.entity.Retrospect;
import com.nanal.backend.domain.retrospect.exception.RetrospectNotFoundException;
import com.nanal.backend.domain.retrospect.exception.RetrospectTimeDoneException;
import com.nanal.backend.domain.retrospect.repository.RetrospectKeywordRepository;
import com.nanal.backend.domain.retrospect.repository.retrospect.RetrospectRepository;
import com.nanal.backend.domain.retrospect.v2.dto.req.ReqGetRetroDto;
import com.nanal.backend.domain.retrospect.v2.dto.resp.RespGetInfoDto;
import com.nanal.backend.global.exception.customexception.MemberAuthException;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Timed("retrospect.api")
@EnableScheduling
@RequiredArgsConstructor
@Transactional
@Service
public class RetrospectServiceV2 {
    private final MemberRepository memberRepository;
    private final RetrospectRepository retrospectRepository;
    private final RetrospectKeywordRepository retrospectKeywordRepository;
    private final DiaryRepository diaryRepository;

    @Counted("retrospect.api.count")
    public RespGetInfoDto getInfo(String socialId, ReqGetInfoDto reqGetInfoDto) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> MemberAuthException.EXCEPTION);

        RespGetInfoDto info = getRespGetInfoDto(reqGetInfoDto, member);

        return info;
    }

    @Counted("retrospect.api.count")
    public RespGetRetroDto getRetro(ReqGetRetroDto reqSearchRetroDto) {
        //조회할 회고 찾기
        Retrospect selectRetrospect = retrospectRepository.findById(reqSearchRetroDto.getRetrospectId()).orElseThrow(() -> RetrospectNotFoundException.EXCEPTION);
        // 몇 주차인지 계산
        Integer week = countWeek(selectRetrospect.getWriteDate());
        // 몇번째 회고인지 조회한 후, 회고 리스트로 반환값 생성
        return RespGetRetroDto.createRespGetRetroDto(selectRetrospect, week);
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
        Retrospect selectRetrospect = retrospectRepository.findRetrospectByMemberAndRetrospectId(member.getMemberId(), reqEditRetroDto.getRetrospectId());

        selectRetrospect.changeAnswer(reqEditRetroDto.getIndex(), reqEditRetroDto.getAnswer());
    }

    @Counted("retrospect.api.count")
    public void deleteRetro(String socialId, ReqDeleteRetroDto reqDeleteRetroDto) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> MemberAuthException.EXCEPTION);
        // 삭제할 회고 가져오기
        Retrospect deleteRetro = retrospectRepository.findRetrospectByMemberAndRetrospectId(member.getMemberId(), reqDeleteRetroDto.getRetrospectId());
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
    //회고 작성 예외처리
    private void checkWriteTime(Member member, LocalDateTime dateTime) {
        // 요일과 날짜가 모두 일치하는지 체크
        LocalDate prevRetroDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(member.getRetrospectDay()));
        if (!prevRetroDate.equals(dateTime.toLocalDate()))
            throw RetrospectTimeDoneException.EXCEPTION;
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
    private boolean checkExistRetro(Member member, LocalDateTime currentDate) {
        String yearMonthDay = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "%";
        return retrospectRepository.findListByMemberAndWriteDate(member.getMemberId(), yearMonthDay).size() != 0;
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
    private void changeDiaryEditStatusToTrue (Member member, LocalDateTime prevRetroDate, LocalDateTime currentTime) {
        diaryRepository.findDiaryListByMemberAndBetweenWriteDate(member.getMemberId(), prevRetroDate.toLocalDate().minusDays(6), currentTime.toLocalDate(), false)
                .forEach(d -> d.changeEditStatus(true));
    }
}
