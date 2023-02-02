package com.nanal.backend.domain.mypage.service;

import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.domain.auth.repository.MemberRepository;
import com.nanal.backend.domain.mypage.repository.FeedbackRepository;
import com.nanal.backend.domain.mypage.dto.req.ReqEditNicknameDto;
import com.nanal.backend.domain.mypage.dto.req.ReqEditRetrospectDayDto;
import com.nanal.backend.domain.mypage.dto.req.ReqWithdrawMembership;
import com.nanal.backend.domain.mypage.dto.resp.*;
import com.nanal.backend.domain.mypage.entity.Feedback;
import com.nanal.backend.global.exception.customexception.MemberAuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


@RequiredArgsConstructor
@Transactional
@Service
public class MypageService {

    private final MemberRepository memberRepository;
    private final FeedbackRepository feedbackRepository;

    public RespGetUserDto getUser(String socialId) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> MemberAuthException.EXCEPTION);

        return RespGetUserDto.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .retrospectDay(member.getRetrospectDay())
                .build();
    }

    public void updateNickname(String socialId, ReqEditNicknameDto reqEditNickname) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> MemberAuthException.EXCEPTION);

        member.updateNickname(reqEditNickname.getNickname());
    }

    public RespCheckChangeAvailability checkChangeAvailability(String socialId) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> MemberAuthException.EXCEPTION);

        LocalDateTime now = LocalDateTime.now();
        if(member.verifyChangingRetrospectDate(now))
            return RespCheckChangeAvailability.changeable(now.plusDays(30), member.getRetrospectDay());
        else
            return RespCheckChangeAvailability.unchangeable(member.getPrevRetrospectDate().plusDays(30));
    }


    public void updateRetrospectDay(String socialId, ReqEditRetrospectDayDto reqEditRetrospectDayDto) {

        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> MemberAuthException.EXCEPTION);

        member.updateRetrospectDay(DayOfWeek.valueOf(reqEditRetrospectDayDto.getRetrospectDay()));
    }

    public RespGetServiceLife getServiceLife(String socialId) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> MemberAuthException.EXCEPTION);

        Integer serviceLife = member.getServiceLife();

        return RespGetServiceLife.builder()
                .serviceLife(serviceLife)
                .build();
    }

    public void withdrawMembership(String socialId, ReqWithdrawMembership reqWithdrawMembership) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> MemberAuthException.EXCEPTION);

        for(ReqWithdrawMembership.Reason r : reqWithdrawMembership.getReasons()){
            Feedback feedback = Feedback.builder()
                    .memberId(member.getMemberId())
                    .type("reason")
                    .content(r.getContent())
                    .build();

            feedbackRepository.save(feedback);
        }

        Feedback detail = Feedback.builder()
                .memberId(member.getMemberId())
                .type("detail")
                .content(reqWithdrawMembership.getDetail())
                .build();

        feedbackRepository.save(detail);

        memberRepository.delete(member);
    }
}
