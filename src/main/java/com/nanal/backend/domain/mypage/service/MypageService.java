package com.nanal.backend.domain.mypage.service;

import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.domain.auth.repository.MemberRepository;
import com.nanal.backend.domain.mypage.dto.req.ReqEditNicknameDto;
import com.nanal.backend.domain.mypage.dto.req.ReqEditRetrospectDayDto;
import com.nanal.backend.domain.mypage.dto.resp.*;
import com.nanal.backend.global.exception.customexception.MemberAuthException;
import com.nanal.backend.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@Transactional
@Service
public class MypageService {

    private final MemberRepository memberRepository;

    public RespGetUserDto getUser(String socialId) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> new MemberAuthException(ErrorCode.MEMBER_NOT_FOUND.getMessage()));

        return RespGetUserDto.builder()
                .userEmail(member.getEmail())
                .userNickname(member.getNickname())
                .userRetrospectDay(member.getRetrospectDay())
                .build();
    }

    public RespEditNicknameDto updateNickname(String socialId, ReqEditNicknameDto reqEditNickname) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> new MemberAuthException(ErrorCode.MEMBER_NOT_FOUND.getMessage()));

        member.updateNickname(reqEditNickname.getNickname());

        return RespEditNicknameDto.builder()
                .nickname(member.getNickname())
                .build();
    }

    public RespCheckChangeAvailability checkChangeAvailability(String socialId) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> new MemberAuthException(ErrorCode.MEMBER_NOT_FOUND.getMessage()));

        LocalDateTime now = LocalDateTime.now();
        member.verifyPrevRetrospectDate(now);

        return RespCheckChangeAvailability.builder()
                .nextChangeableDate(now.plusDays(30))
                .curRetrospectDay(member.getRetrospectDay())
                .build();
    }

    public RespEditRetrospectDayDto updateRetrospectDay(String socialId, ReqEditRetrospectDayDto reqEditRetrospectDayDto) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> new MemberAuthException(ErrorCode.MEMBER_NOT_FOUND.getMessage()));

        LocalDateTime now = LocalDateTime.now();
        member.updateRetrospectDay(reqEditRetrospectDayDto.getRetrospectDay(), now);

        return RespEditRetrospectDayDto.builder()
                .updatedRetrospectDay(reqEditRetrospectDayDto.getRetrospectDay())
                .build();
    }

    public RespGetServiceLife getServiceLife(String socialId) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> new MemberAuthException(ErrorCode.MEMBER_NOT_FOUND.getMessage()));

        Integer serviceLife = member.getServiceLife();

        return RespGetServiceLife.builder()
                .serviceLife(serviceLife)
                .build();
    }
}
