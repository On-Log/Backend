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
import com.nanal.backend.global.security.jwt.TokenUtil;
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

    private final TokenUtil tokenUtil;
    private final MemberRepository memberRepository;
    private final FeedbackRepository feedbackRepository;

    @Transactional(readOnly = true)
    public RespGetUserDto getUser(String socialId) {
        // socialId 로 유저 조회
        Member member = findMember(socialId);

        return RespGetUserDto.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .retrospectDay(member.getRetrospectDay())
                .build();
    }

    public void updateNickname(String socialId, ReqEditNicknameDto reqEditNickname) {
        // socialId 로 유저 조회
        Member member = findMember(socialId);

        member.updateNickname(reqEditNickname.getNickname());
    }

    @Transactional(readOnly = true)
    public RespCheckChangeAvailability checkChangeAvailability(String socialId) {
        // socialId 로 유저 조회
        Member member = findMember(socialId);

        LocalDateTime now = LocalDateTime.now();
        if(member.verifyChangingRetrospectDate(now))
            return RespCheckChangeAvailability.changeable(now.plusDays(30), member.getRetrospectDay());
        else
            return RespCheckChangeAvailability.unchangeable(member.getPrevRetrospectDate().plusDays(30));
    }

    public void updateRetrospectDay(String socialId, ReqEditRetrospectDayDto reqEditRetrospectDayDto) {
        // socialId 로 유저 조회
        Member member = findMember(socialId);

        member.updateRetrospectDay(DayOfWeek.valueOf(reqEditRetrospectDayDto.getRetrospectDay()));
    }

    @Transactional(readOnly = true)
    public RespGetServiceLife getServiceLife(String socialId) {
        // socialId 로 유저 조회
        Member member = findMember(socialId);

        Integer serviceLife = member.getServiceLife();

        return RespGetServiceLife.builder()
                .serviceLife(serviceLife)
                .build();
    }

    @Transactional(readOnly = true)
    public void logout(String socialId) {
        // socialId 로 유저 조회
        Member member = findMember(socialId);

        tokenUtil.expireRefreshToken(member.getSocialId());
    }

    public void withdrawMembership(String socialId, ReqWithdrawMembership reqWithdrawMembership) {
        // socialId 로 유저 조회
        Member member = findMember(socialId);

        saveWithdrawalReasons(reqWithdrawMembership, member);

        memberRepository.delete(member);
    }

    //=== 편의 메서드 ===//

    private Member findMember(String socialId) {
        return memberRepository.findBySocialId(socialId).orElseThrow(() -> MemberAuthException.EXCEPTION);
    }

    private void saveWithdrawalReasons(ReqWithdrawMembership reqWithdrawMembership, Member member) {
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
    }
}
