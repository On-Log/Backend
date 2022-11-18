package com.nanal.backend.domain.retrospect.controller;

import com.nanal.backend.config.response.CommonResponse;
import com.nanal.backend.config.response.ErrorCode;
import com.nanal.backend.domain.oauth.UserDto;
import com.nanal.backend.domain.retrospect.dto.*;
import com.nanal.backend.domain.retrospect.service.RetrospectService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class RetrospectController {

    private final RetrospectService retrospectService;

    /**
     * 회고 탭 화면
     * [GET] /retrospect
     * 작성자 : 장세은
     * 수정일 :
     //     */
    @GetMapping("/retrospect")
    public CommonResponse<RespGetInfoDto> getInfo(@AuthenticationPrincipal UserDto userDto, @RequestBody ReqGetInfoDto reqGetInfoDto) {

        // 요청 정보 기반으로 해당 날짜에 맞는 정보 조회
        RespGetInfoDto respGetInfoDto = retrospectService.getInfo(userDto.getEmail(), reqGetInfoDto);

        return new CommonResponse<>(respGetInfoDto);
    }

    /**
     * 회고 기록
     * [POST] /retrospect
     * 작성자 : 장세은
     * 수정일 :
     */
    @PostMapping("/retrospect")
    public CommonResponse<?> saveRetrospect(@AuthenticationPrincipal UserDto userDto, @RequestBody ReqSaveRetroDto reqSaveRetroDto) {

        //     요청 날짜 기반으로 회고 기록
        retrospectService.saveRetrospect(userDto.getEmail(), reqSaveRetroDto);

        return new CommonResponse<>(ErrorCode.SUCCESS);
    }

    /**
     * 회고 조회
     * [GET] /retrospect/view
     * 작성자 : 장세은
     * 수정일 :
     */
    @GetMapping("/retrospect/view")
    public CommonResponse<RespGetRetroDto> getRetrospect(@AuthenticationPrincipal UserDto userDto, @RequestBody ReqGetRetroDto reqGetRetroDto) {

        //     요청 날짜 기반으로 회고 조회
        RespGetRetroDto respGetRetroDto = retrospectService.getRetro(userDto.getEmail(), reqGetRetroDto);

        return new CommonResponse<>(respGetRetroDto);
    }

    /**
     * 회고 수정
     * [PUT] /retrospect
     * 작성자 : 장세은
     * 수정일 :
     */
    @PutMapping("/retrospect")
    public CommonResponse<?> editRetrospect(@AuthenticationPrincipal UserDto userDto, @RequestBody ReqEditRetroDto reqEditRetroDto) {

        retrospectService.editRetrospect(userDto.getEmail(), reqEditRetroDto);
        return new CommonResponse<>(ErrorCode.SUCCESS);
    }


}
