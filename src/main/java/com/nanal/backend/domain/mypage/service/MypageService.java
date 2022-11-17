package com.nanal.backend.domain.mypage.service;

import com.nanal.backend.domain.mypage.controller.MypageController;
import com.nanal.backend.domain.mypage.dto.*;
import com.nanal.backend.domain.mypage.repository.MemberRepository;
import com.nanal.backend.domain.oauth.UserDto;
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
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new RuntimeException());

        return RespGetUserDto.builder()
                .userEmail(member.getEmail())
                .userNickname(member.getNickname())
                .userRetrospectDay(member.getRetrospectDay())
                .build();
    }

    public RespEditNicknameDto getNickname(String email, ReqEditNicknameDto reqEditNicknameDto) {

        // email 로 유저 조회
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new RuntimeException());

        return RespEditNicknameDto.builder()
                .userNickname(member.getNickname())
                .build();
    }

    public RespEditRetrospectDayDto getRetrospectDay(String email, ReqEditRetrospectDayDto reqEditRetrospectDayDto) {

        // email 로 유저 조회
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new RuntimeException());

        return RespEditRetrospectDayDto.builder()
                .userRetrospectDay(member.getRetrospectDay())
                .build();
    }

    public boolean checkRetrospectDay(String email, DayOfWeek retrospectDay) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new RuntimeException());
        if(member.getRetrospectDay().equals(retrospectDay)) //기존 값이 받은 값과 같으면
                return true;
        return false;
    }

    public boolean checkResetAvail(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new RuntimeException());

        if(member.getResetAvail() == false)
            return true;

        member.setResetAvail(false);
        return false;
    }

    @Transactional
    public RespEditNicknameDto updateNickname(UserDto userDto, ReqEditNicknameDto reqEditNickname) {
        // email 로 유저 조회
        Member member = memberRepository.findByEmail(userDto.getEmail()).orElseThrow(() -> new RuntimeException());

        member.updateNickname(reqEditNickname.getNickname());

        return RespEditNicknameDto.builder()
                .userNickname(member.getNickname())
                .build();
    }

    @Transactional
    public RespEditRetrospectDayDto updateRetrospectDay(UserDto userDto, ReqEditRetrospectDayDto reqEditRetrospectDay) { //reqEditNickname string으로 변경함. -> 다시 dto.. ReqEditNicknameDto reqEditNickname 이거 아닌듯..?
        // email 로 유저 조회
        Member member = memberRepository.findByEmail(userDto.getEmail()).orElseThrow(() -> new RuntimeException());

        member.updateRetrospectDay(reqEditRetrospectDay.getRetrospectDay());

        return RespEditRetrospectDayDto.builder()
                .userRetrospectDay(member.getRetrospectDay())
                .build();
    }
}
