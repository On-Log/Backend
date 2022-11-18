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
}
