package com.nanal.backend.domain.mypage.service;

import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.domain.auth.repository.MemberRepository;
import com.nanal.backend.domain.mypage.dto.req.ReqEditNicknameDto;
import com.nanal.backend.domain.mypage.dto.req.ReqEditRetrospectDayDto;
import com.nanal.backend.domain.mypage.dto.resp.RespEditNicknameDto;
import com.nanal.backend.domain.mypage.dto.resp.RespEditRetrospectDayDto;
import com.nanal.backend.domain.mypage.dto.resp.RespGetUserDto;
import com.nanal.backend.domain.mypage.exception.ResetAvailException;
import com.nanal.backend.domain.mypage.exception.RetrospectDayDupException;
import com.nanal.backend.global.exception.customexception.MemberAuthException;
import com.nanal.backend.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;


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

    @Transactional
    public RespEditNicknameDto updateNickname(String socialId, ReqEditNicknameDto reqEditNickname) {
        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> new MemberAuthException(ErrorCode.MEMBER_NOT_FOUND.getMessage()));

        member.setNickname(reqEditNickname.getNickname());

        return RespEditNicknameDto.builder()
                .userNickname(member.getNickname())
                .build();
    }

    @Transactional
    public RespEditRetrospectDayDto updateRetrospectDay(String socialId, ReqEditRetrospectDayDto reqEditRetrospectDayDto) {

        // socialId 로 유저 조회
        Member member = memberRepository.findBySocialId(socialId).orElseThrow(() -> new MemberAuthException(ErrorCode.MEMBER_NOT_FOUND.getMessage()));

        // 회고일이 같은 경우, error.
        if (checkRetrospectDay(member, reqEditRetrospectDayDto.getRetrospectDay())) {throw new RetrospectDayDupException(ErrorCode.RETROSPECT_DAY_DUPLICATION.getMessage());}
        // resetAvail이 false일 때(= 회고일 변경으로부터 한 달이 지나지 않아 변경할 수 없을 때.), error.
        if (checkResetAvail(member)) {throw new ResetAvailException(ErrorCode.RESET_AVAIL_FALSE.getMessage());}

        member.setRetrospectDay(reqEditRetrospectDayDto.getRetrospectDay());

        return RespEditRetrospectDayDto.builder()
                .userRetrospectDay(member.getRetrospectDay())
                .build();
    }

    //===편의 메서드===//

    public boolean checkRetrospectDay(Member member, DayOfWeek retrospectDay) {
        if(member.getRetrospectDay().equals(retrospectDay)) //기존 값이 받은 값과 같으면
            return true;
        return false;
    }

    public boolean checkResetAvail(Member member) {
        if(member.getResetAvail() == false) return true;

        member.setResetAvail(false);
        return false;
    }
}
