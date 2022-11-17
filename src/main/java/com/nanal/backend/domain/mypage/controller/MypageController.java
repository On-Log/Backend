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
     * 수정일 : 2022-11-16
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
     * 수정일 : 2022-11-17
     */
    @PutMapping("/mypage/nickname")
    public CommonResponse<RespEditNicknameDto> updateNickname(@AuthenticationPrincipal UserDto userDto, @RequestBody @Valid ReqEditNicknameDto reqEditNickname) {
        mypageService.updateNickname(userDto, reqEditNickname);
        RespEditNicknameDto respEditNicknameDto = mypageService.getNickname(userDto.getEmail(), reqEditNickname);

        return new CommonResponse<>(respEditNicknameDto);
    }

    /**
     * 회고요일 변경
     * [PUT] /mypage/day
     * 작성자 : 김유빈
     * 수정일 : 2022-11-17
     */
    @PutMapping("/mypage/day")
    public CommonResponse<RespEditRetrospectDayDto> updateRetrospectDay(@AuthenticationPrincipal UserDto userDto, @RequestBody @Valid ReqEditRetrospectDayDto reqEditRetrospectDayDto) {
        if (mypageService.checkRetrospectDay(userDto.getEmail(), reqEditRetrospectDayDto.getRetrospectDay())) {
            // 회고일이 같은 경우, error.
            return new CommonResponse(ErrorCode.RETROSPECTDAY_DUPLICATION);
        }
        if (mypageService.checkResetAvail(userDto.getEmail())) {
            // resetAvail이 false일 때(= 회고일 변경으로부터 한 달이 지나지 않아 변경할 수 없을 때.), error.
            return new CommonResponse(ErrorCode.RESETAVAIL_FALSE);
        }
        mypageService.updateRetrospectDay(userDto, reqEditRetrospectDayDto);
        RespEditRetrospectDayDto respEditRetrospectDayDto = mypageService.getRetrospectDay(userDto.getEmail(), reqEditRetrospectDayDto);
        return new CommonResponse<>(respEditRetrospectDayDto);

    }

}
