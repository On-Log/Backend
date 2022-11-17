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

    /*public boolean checkRetrospectDay(String email, DayOfWeek retrospectDay) {

        if(memberRepository.existsByRetrospectDay(retrospectDay )){
            if(memberRepository.findByRetrospectDay(retrospectDay).getId() == member_id){ - 변경
                return true;
        }
    }*/


    //public Member findOne(String nickname) { return memberRepository.findByNickName(nickname);}

    @Transactional
    public RespEditNicknameDto update(UserDto userDto, ReqEditNicknameDto reqEditNickname) { //reqEditNickname string으로 변경함. -> 다시 dto.. ReqEditNicknameDto reqEditNickname 이거 아닌듯..?
        // email 로 유저 조회
        // 그냥,, 안됨 Member member = memberRepository.findByEmail(email); //memberrepository 사용하지 않기.. - findOne으로 이메일 찾기 - findOne 안됨. 그냥 memberrepo로..
        Member member = memberRepository.findByEmail(userDto.getEmail()).orElseThrow(() -> new RuntimeException());
        System.out.println("---------service 단 "+"1. UserDto getemail 확인: "+userDto.getEmail()+"2. getNickname 확인: "+reqEditNickname.getNickname());
        member.update(reqEditNickname.getNickname()); //member 업데이트 진행. -> UserDto로 변경. -> member로.

        // return member;
        return RespEditNicknameDto.builder()
                .userNickname(member.getNickname())
                .build();

        //System.out.println("reqEdit 맵핑 확인: "+member.getName()+"\n 매핑 이름 확인"+nickname); //닉네임 못받음. -> controller단 문제? reqEditNickname.getNickname() 이거 버림.
        //.. member.setNickname(nickname);
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
        // return member;
    }
}
