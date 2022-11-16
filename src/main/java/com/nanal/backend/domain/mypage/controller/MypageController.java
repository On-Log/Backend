package com.nanal.backend.domain.mypage.controller;

import com.nanal.backend.config.response.CommonResponse;
import com.nanal.backend.config.response.ErrorCode;
import com.nanal.backend.domain.diary.dto.*;
import com.nanal.backend.domain.diary.service.DiaryService;
// import com.nanal.backend.domain.mypage.dto.ReqEditNicknameDto; - 현재 미사용
import com.nanal.backend.domain.mypage.dto.*;
import com.nanal.backend.domain.mypage.service.MypageService;
import com.nanal.backend.domain.oauth.UserDto;
import com.nanal.backend.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RequiredArgsConstructor
@RestController
public class MypageController {

    private final MypageService mypageService;

    /**
     * 마이페이지 정보
     * [GET] /mypage
     * 작성자 : 김유빈
     * 수정일 : 2022-11-11
     */
    @GetMapping("/mypage")
    public CommonResponse<RespGetUserDto> getUser(@AuthenticationPrincipal UserDto userDto, ReqGetUserDto reqGetUserDto) {

        // 유저 정보 조회 - diary와 동일.
        RespGetUserDto respGetUserDto = mypageService.getUser(userDto.getEmail(), reqGetUserDto);

        return new CommonResponse<>(respGetUserDto);
    }

    /**
     * 닉네임 변경
     * [PUT] /mypage/nickname
     * 작성자 : 김유빈
     * 수정일 : 2022-11-16
     */
    @PutMapping("/mypage/nickname") //putmapping을 통해 url 매핑.
    public CommonResponse<RespEditNicknameDto> update(@AuthenticationPrincipal UserDto userDto, @RequestBody @Valid ReqEditNicknameDto reqEditNickname) { //일단 보류 ResEditMemberDto -> UserDto 가 아닌가,,?
        System.out.println("------Controller단 1. userDto 확인: "+userDto+" 2. req확인"+reqEditNickname);
        mypageService.update(userDto, reqEditNickname); //remove return

        RespEditNicknameDto respEditNicknameDto = mypageService.getNickname(userDto.getEmail(), reqEditNickname);

        return new CommonResponse<>(respEditNicknameDto);
    }

    /**
     * 회고요일 변경
     * [PUT] /mypage/day
     * 작성자 : 김유빈
     * 수정일 : 2022-11-16
     */
    @PutMapping("/mypage/day") //putmapping을 통해 url 매핑.
    public CommonResponse<RespEditRetrospectDayDto> updateRetrospectDay(@AuthenticationPrincipal UserDto userDto, @RequestBody @Valid ReqEditRetrospectDayDto reqEditRetrospectDayDto) { //일단 보류 ResEditMemberDto -> UserDto 가 아닌가,,?
        System.out.println("------Controller단 1. userDto 확인: " + userDto + " 2. req확인" + reqEditRetrospectDayDto);

        /*if(mypageService.checkRetrospectDay(userDto.getEmail(), reqEditRetrospectDayDto.getRetrospectDay())){
            //log.info("same retrospectDay");
            return new CommonResponse(true);*/

        mypageService.updateRetrospectDay(userDto, reqEditRetrospectDayDto);

        RespEditRetrospectDayDto respEditRetrospectDayDto = mypageService.getRetrospectDay(userDto.getEmail(), reqEditRetrospectDayDto);

        return new CommonResponse<>(respEditRetrospectDayDto);
    }

}
