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
    // private final EditMemberRepository editMemberRepository; //findOne에 적용하기 위해 생성. - 이거 아닌거 같음

    public RespGetUserDto getUser(String email, ReqGetUserDto reqGetUserDto) {

        // email 로 유저 조회
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new RuntimeException());

        // responseDto로 build. 이메일과 닉네임, 회고일을 get.
        return RespGetUserDto.builder()
                .userEmail(member.getEmail())
                .userNickname(member.getNickname())
                .userRetrospectDay(member.getRetrospectDay())
                .build();
    }

    public RespEditNicknameDto getNickname(String email, ReqEditNicknameDto reqEditNicknameDto) {

        // email 로 유저 조회
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new RuntimeException());

        // responseDto로 build. 닉네임 get.
        return RespEditNicknameDto.builder()
                .userNickname(member.getNickname())
                .build();
    }

    public RespEditRetrospectDayDto getRetrospectDay(String email, ReqEditRetrospectDayDto reqEditRetrospectDayDto) {

        // email 로 유저 조회
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new RuntimeException());

        // responseDto로 build. 닉네임 get.
        return RespEditRetrospectDayDto.builder()
                .userRetrospectDay(member.getRetrospectDay())
                .build();
    }

    /** retrospectDay - **/

    public boolean checkRetrospectDay(String email, DayOfWeek retrospectDay) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new RuntimeException());
        System.out.println("받는 값: "+member.getRetrospectDay()+"---- 그냥 retrospect"+retrospectDay);
        if(member.getRetrospectDay().equals(retrospectDay)) //기존 값이 받은 값과 같으면
                return true;
        return false;
    }

    public boolean checkResetAvail(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new RuntimeException());
        System.out.println("-------- 회고일 받기: "+member.getResetAvail());
        if(member.getResetAvail() == false)
            return true;

        System.out.println("-------- 회고일 받기: "+member.getResetAvail());
        member.setResetAvail(false);
        return false;
    }

    @Transactional
    public RespEditNicknameDto update(UserDto userDto, ReqEditNicknameDto reqEditNickname) {
        // email 로 유저 조회
        Member member = memberRepository.findByEmail(userDto.getEmail()).orElseThrow(() -> new RuntimeException());
        System.out.println("---------service 단 "+"1. UserDto getemail 확인: "+userDto.getEmail()+"2. getNickname 확인: "+reqEditNickname.getNickname());
        member.update(reqEditNickname.getNickname());

        return RespEditNicknameDto.builder()
                .userNickname(member.getNickname())
                .build();
    }

    @Transactional
    public RespEditRetrospectDayDto updateRetrospectDay(UserDto userDto, ReqEditRetrospectDayDto reqEditRetrospectDay) { //reqEditNickname string으로 변경함. -> 다시 dto.. ReqEditNicknameDto reqEditNickname 이거 아닌듯..?
        // email 로 유저 조회
        Member member = memberRepository.findByEmail(userDto.getEmail()).orElseThrow(() -> new RuntimeException());

        System.out.println("---------service 단 "+"1. UserDto getemail 확인: "+userDto.getEmail()+"2. getNickname 확인: "+reqEditRetrospectDay.getRetrospectDay());
        member.updateRetrospectDay(reqEditRetrospectDay.getRetrospectDay()); //member 업데이트 진행. -> UserDto로 변경.

        return RespEditRetrospectDayDto.builder()
                .userRetrospectDay(member.getRetrospectDay())
                .build();
    }
}
