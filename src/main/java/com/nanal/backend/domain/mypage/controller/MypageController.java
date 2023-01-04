package com.nanal.backend.domain.mypage.controller;

import com.nanal.backend.domain.mypage.dto.req.ReqEditNicknameDto;
import com.nanal.backend.domain.mypage.dto.req.ReqEditRetrospectDayDto;
import com.nanal.backend.domain.mypage.dto.req.ReqGetUserDto;
import com.nanal.backend.domain.mypage.dto.resp.RespEditNicknameDto;
import com.nanal.backend.domain.mypage.dto.resp.RespEditRetrospectDayDto;
import com.nanal.backend.domain.mypage.dto.resp.RespGetUserDto;
import com.nanal.backend.domain.mypage.service.MypageService;
import com.nanal.backend.global.response.CommonResponse;
import com.nanal.backend.global.security.User;
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
    public CommonResponse<RespGetUserDto> getUser(@AuthenticationPrincipal User user,
                                                  @Valid ReqGetUserDto reqGetUserDto) {

        // 유저 정보 조회
        RespGetUserDto respGetUserDto = mypageService.getUser(user.getSocialId(), reqGetUserDto);

        return new CommonResponse<>(respGetUserDto);
    }

    /**
     * 닉네임 변경
     * [PUT] /mypage/nickname
     * 작성자 : 김유빈
     * 수정일 : 2022-11-17
     */
    @PutMapping("/mypage/nickname")
    public CommonResponse<RespEditNicknameDto> updateNickname(@AuthenticationPrincipal User user,
                                                              @RequestBody @Valid ReqEditNicknameDto reqEditNickname) {

        // 닉네임 변경
        RespEditNicknameDto respEditNicknameDto = mypageService.updateNickname(user.getSocialId(), reqEditNickname);

        return new CommonResponse<>(respEditNicknameDto);
    }

    /**
     * 회고요일 변경
     * [PUT] /mypage/day
     * 작성자 : 김유빈
     * 수정일 : 2022-11-17
     */
    @PutMapping("/mypage/day")
    public CommonResponse<RespEditRetrospectDayDto> updateRetrospectDay(@AuthenticationPrincipal User user,
                                                                        @RequestBody @Valid ReqEditRetrospectDayDto reqEditRetrospectDayDto) {

        // 회고요일 변경
        RespEditRetrospectDayDto respEditRetrospectDayDto = mypageService.updateRetrospectDay(user.getSocialId(), reqEditRetrospectDayDto);

        return new CommonResponse<>(respEditRetrospectDayDto);
    }

}
