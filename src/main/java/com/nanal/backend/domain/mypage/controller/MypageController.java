package com.nanal.backend.domain.mypage.controller;

import com.nanal.backend.global.response.CommonResponse;
import com.nanal.backend.domain.mypage.dto.*;
import com.nanal.backend.domain.mypage.service.MypageService;
import com.nanal.backend.global.auth.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RequiredArgsConstructor
@RestController
@Tag(name = "MypageController", description = "마이페이지 관련 api")

public class MypageController {

    private final MypageService mypageService;

    /**
     * 마이페이지 정보
     * [GET] /mypage
     * 작성자 : 김유빈
     * 수정일 : 2022-11-16
     */
    @Operation(summary="마이페이지 정보 조회", description="유저 정보 기반 마이페이지 조회")
    @GetMapping("/mypage")
    public CommonResponse<RespGetUserDto> getUser(@Parameter(hidden = true) @AuthenticationPrincipal UserDto userDto,
                                                  @Valid ReqGetUserDto reqGetUserDto) {

        // 유저 정보 조회
        RespGetUserDto respGetUserDto = mypageService.getUser(userDto.getEmail(), reqGetUserDto);

        return new CommonResponse<>(respGetUserDto);
    }

    /**
     * 닉네임 변경
     * [PUT] /mypage/nickname
     * 작성자 : 김유빈
     * 수정일 : 2022-11-17
     */
    @Operation(summary="닉네임 변경", description="닉네임 변경")
    @PutMapping("/mypage/nickname")
    public CommonResponse<RespEditNicknameDto> updateNickname(@Parameter(hidden = true) @AuthenticationPrincipal UserDto userDto,
                                                              @RequestBody @Valid ReqEditNicknameDto reqEditNickname) {

        // 닉네임 변경
        RespEditNicknameDto respEditNicknameDto = mypageService.updateNickname(userDto, reqEditNickname);

        return new CommonResponse<>(respEditNicknameDto);
    }

    /**
     * 회고요일 변경
     * [PUT] /mypage/day
     * 작성자 : 김유빈
     * 수정일 : 2022-11-17
     */
    @Operation(summary="회고요일 변경", description="회고요일 변경")
    @PutMapping("/mypage/day")
    public CommonResponse<RespEditRetrospectDayDto> updateRetrospectDay(@Parameter(hidden = true) @AuthenticationPrincipal UserDto userDto,
                                                                        @RequestBody @Valid ReqEditRetrospectDayDto reqEditRetrospectDayDto) {

        // 회고요일 변경
        RespEditRetrospectDayDto respEditRetrospectDayDto = mypageService.updateRetrospectDay(userDto, reqEditRetrospectDayDto);

        return new CommonResponse<>(respEditRetrospectDayDto);
    }

}
