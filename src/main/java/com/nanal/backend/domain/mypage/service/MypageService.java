package com.nanal.backend.domain.mypage.service;

import com.nanal.backend.global.exception.customexception.MemberAuthException;
import com.nanal.backend.global.exception.customexception.ResetAvailException;
import com.nanal.backend.global.exception.customexception.RetrospectDayDupException;
import com.nanal.backend.domain.mypage.dto.*;
import com.nanal.backend.domain.mypage.repository.MemberRepository;
import com.nanal.backend.global.auth.oauth.UserDto;
import com.nanal.backend.entity.Member;
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
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberAuthException("마이페이지 정보 요청"));

        return RespGetUserDto.builder()
                .userEmail(member.getEmail())
                .userNickname(member.getNickname())
                .userRetrospectDay(member.getRetrospectDay())
                .build();
    }

    @Transactional
    public RespEditNicknameDto updateNickname(UserDto userDto, ReqEditNicknameDto reqEditNickname) {
        // email 로 유저 조회
        Member member = memberRepository.findByEmail(userDto.getEmail()).orElseThrow(() -> new MemberAuthException("닉네임 변경 요청"));

        member.changeNickname(reqEditNickname.getNickname());

        return RespEditNicknameDto.builder()
                .userNickname(member.getNickname())
                .build();
    }

    @Transactional
    public RespEditRetrospectDayDto updateRetrospectDay(UserDto userDto, ReqEditRetrospectDayDto reqEditRetrospectDayDto) {

        // email 로 유저 조회
        Member member = memberRepository.findByEmail(userDto.getEmail()).orElseThrow(() -> new MemberAuthException("회고요일 변경 요청"));

        // 회고일이 같은 경우, error.
        if (checkRetrospectDay(member, reqEditRetrospectDayDto.getRetrospectDay())) {throw new RetrospectDayDupException("회고요일 변경 요청");}
        // resetAvail이 false일 때(= 회고일 변경으로부터 한 달이 지나지 않아 변경할 수 없을 때.), error.
        if (checkResetAvail(member)) {throw new ResetAvailException("회고요일 변경 요청");}

        member.changeRetrospectDay(reqEditRetrospectDayDto.getRetrospectDay());

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
