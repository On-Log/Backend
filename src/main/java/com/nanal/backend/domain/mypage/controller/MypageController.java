package com.nanal.backend.domain.mypage.controller;

import com.nanal.backend.domain.mypage.dto.req.ReqEditNicknameDto;
import com.nanal.backend.domain.mypage.dto.req.ReqEditRetrospectDayDto;
import com.nanal.backend.domain.mypage.dto.resp.*;
import com.nanal.backend.domain.mypage.service.MypageService;
import com.nanal.backend.global.response.CommonResponse;
import com.nanal.backend.global.response.ErrorCode;
import com.nanal.backend.global.security.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;


@RequiredArgsConstructor
@RestController
public class MypageController {

    private final MypageService mypageService;

    /**
     * 마이페이지 화면
     * [GET] /mypage
     */
    @GetMapping("/mypage")
    public CommonResponse<?> getUser(@AuthenticationPrincipal User user) {

        // 유저 정보 조회
        RespGetUserDto respGetUserDto = mypageService.getUser(user.getSocialId());

        return new CommonResponse<>(respGetUserDto);
    }

    /**
     * 닉네임 변경
     * [PUT] /mypage/nickname
     */
    @PutMapping("/mypage/nickname")
    public CommonResponse<?> updateNickname(@AuthenticationPrincipal User user,
                                                              @RequestBody @Valid ReqEditNicknameDto reqEditNickname) {

        // 닉네임 변경
        RespEditNicknameDto respEditNicknameDto = mypageService.updateNickname(user.getSocialId(), reqEditNickname);

        return new CommonResponse<>(respEditNicknameDto);
    }

    /**
     * 회고일 변경 가능 여부
     * [GET] /mypage/retrospect
     */
    @GetMapping("/mypage/retrospect")
    public CommonResponse<?> checkChangeAvailability(@AuthenticationPrincipal User user) {

        // 회고일 변경 가능 여부
        RespCheckChangeAvailability respCheckChangeAvailability = mypageService.checkChangeAvailability(user.getSocialId());

        return new CommonResponse<>(ErrorCode.SUCCESS, respCheckChangeAvailability);
    }

    /**
     * 회고일 변경
     * [PUT] /mypage/retrospect
     */
    @PutMapping("/mypage/retrospect")
    public CommonResponse<?> updateRetrospectDay(@AuthenticationPrincipal User user,
                                                                        @RequestBody @Valid ReqEditRetrospectDayDto reqEditRetrospectDayDto) {

        // 회고요일 변경
        RespEditRetrospectDayDto respEditRetrospectDayDto = mypageService.updateRetrospectDay(user.getSocialId(), reqEditRetrospectDayDto);

        return new CommonResponse<>(respEditRetrospectDayDto);
    }

    /**
     * 서비스 사용기간 조회
     */
    @GetMapping("/mypage/service-life")
    public CommonResponse<?> getServiceLife(@AuthenticationPrincipal User user) {

        // 서비스 사용기간 조회
        RespGetServiceLife respGetServiceLife = mypageService.getServiceLife(user.getSocialId());

        return new CommonResponse<>(respGetServiceLife);
    }
}
