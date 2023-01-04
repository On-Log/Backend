package com.nanal.backend.domain.mypage.service;

import com.nanal.backend.domain.mypage.dto.req.ReqEditNicknameDto;
import com.nanal.backend.domain.mypage.dto.req.ReqEditRetrospectDayDto;
import com.nanal.backend.domain.mypage.dto.req.ReqGetUserDto;
import com.nanal.backend.domain.mypage.dto.resp.RespEditNicknameDto;
import com.nanal.backend.domain.mypage.dto.resp.RespEditRetrospectDayDto;
import com.nanal.backend.domain.mypage.dto.resp.RespGetUserDto;
import com.nanal.backend.global.exception.customexception.MemberAuthException;
import com.nanal.backend.domain.mypage.exception.ResetAvailException;
import com.nanal.backend.domain.mypage.exception.RetrospectDayDupException;
import com.nanal.backend.domain.auth.repository.MemberRepository;
import com.nanal.backend.global.security.UserDto;
import com.nanal.backend.domain.auth.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;


@RequiredArgsConstructor
@Transactional
@Service
public class MypageService {

    private final MemberRepository memberRepository;

    public RespGetUserDto getUser(String email, ReqGetUserDto reqGetUserDto) {

        // email 로 유저 조회
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberAuthException("존재하지 않는 유저입니다."));

        return RespGetUserDto.builder()
                .userEmail(member.getEmail())
                .userNickname(member.getNickname())
                .userRetrospectDay(member.getRetrospectDay())
                .build();
    }

    @Transactional
    public RespEditNicknameDto updateNickname(UserDto userDto, ReqEditNicknameDto reqEditNickname) {
        // email 로 유저 조회
        Member member = memberRepository.findByEmail(userDto.getEmail()).orElseThrow(() -> new MemberAuthException("존재하지 않는 유저입니다."));

        member.setNickname(reqEditNickname.getNickname());

        return RespEditNicknameDto.builder()
                .userNickname(member.getNickname())
                .build();
    }

    @Transactional
    public RespEditRetrospectDayDto updateRetrospectDay(UserDto userDto, ReqEditRetrospectDayDto reqEditRetrospectDayDto) {

        // email 로 유저 조회
        Member member = memberRepository.findByEmail(userDto.getEmail()).orElseThrow(() -> new MemberAuthException("존재하지 않는 유저입니다."));

        // 회고일이 같은 경우, error.
        if (checkRetrospectDay(member, reqEditRetrospectDayDto.getRetrospectDay())) {throw new RetrospectDayDupException("이전 회고일과 같은 회고일을 선택했습니다.");}
        // resetAvail이 false일 때(= 회고일 변경으로부터 한 달이 지나지 않아 변경할 수 없을 때.), error.
        if (checkResetAvail(member)) {throw new ResetAvailException("이번 달에 이미 회고일을 변경했습니다.");}

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
