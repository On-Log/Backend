package com.nanal.backend.domain.retrospect.v2.controller;

import com.nanal.backend.domain.retrospect.v2.dto.req.ReqDeleteRetroDto;
import com.nanal.backend.domain.retrospect.v2.dto.req.ReqEditRetroDto;
import com.nanal.backend.domain.retrospect.dto.req.ReqGetInfoDto;
import com.nanal.backend.domain.retrospect.v2.dto.resp.RespGetRetroDto;
import com.nanal.backend.domain.retrospect.v2.dto.req.ReqGetRetroDto;
import com.nanal.backend.domain.retrospect.v2.dto.resp.RespGetInfoDto;
import com.nanal.backend.domain.retrospect.v2.service.RetrospectServiceV2;
import com.nanal.backend.global.exception.customexception.BindingResultException;
import com.nanal.backend.global.response.CommonResponse;
import com.nanal.backend.global.response.ErrorCode;
import com.nanal.backend.global.security.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class RetrospectControllerV2 {

    private final RetrospectServiceV2 retrospectServiceV2;

    /**
     * 회고 탭 화면
     * [GET] /retrospect/v2
     * 작성자 : 장세은
     * 수정일 :
     */
    @GetMapping("/retrospect/v2")
    public CommonResponse<RespGetInfoDto> getInfo(@AuthenticationPrincipal User user,
                                                  @Valid ReqGetInfoDto reqGetInfoDto,
                                                  BindingResult bindingResult) {

        if(bindingResult.hasErrors()) throw new BindingResultException(bindingResult.getFieldErrors());

        // 요청 정보 기반으로 해당 날짜에 맞는 정보 조회
        RespGetInfoDto respGetInfoDto = retrospectServiceV2.getInfo(user.getSocialId(), reqGetInfoDto);

        return new CommonResponse<>(respGetInfoDto);
    }

    /**
     * 회고 조회
     * [GET] /retrospect/view/v2
     * 작성자 : 장세은
     * 수정일 :
     */
    @GetMapping("/retrospect/view/v2")
    public CommonResponse<RespGetRetroDto> getRetrospect(@Valid ReqGetRetroDto reqGetRetroDto,
                                                         BindingResult bindingResult) {
        if(bindingResult.hasErrors()) throw new BindingResultException(bindingResult.getFieldErrors());

        // 요청 날짜 기반으로 회고 조회
        RespGetRetroDto respGetSearchRetroDto = retrospectServiceV2.getRetro(reqGetRetroDto);

        return new CommonResponse<>(respGetSearchRetroDto);
    }

    /**
     * 회고 수정
     * [PUT] /retrospect/v2
     * 작성자 : 장세은
     * 수정일 :
     */
    @PutMapping("/retrospect/v2")
    public CommonResponse<?> editRetrospect(@AuthenticationPrincipal User user,
                                            @RequestBody @Valid ReqEditRetroDto reqEditRetroDto) {

        // 요청 날짜 기반으로 회고 수정
        retrospectServiceV2.editRetrospect(user.getSocialId(), reqEditRetroDto);

        return new CommonResponse<>(ErrorCode.SUCCESS);
    }

    /**
     * 회고 삭제
     * [DELETE] /retrospect/v2
     */
    @DeleteMapping("/retrospect/v2")
    public CommonResponse<?> deleteDiary(@AuthenticationPrincipal User user,
                                         @Valid ReqDeleteRetroDto reqDeleteRetroDto, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) throw new BindingResultException(bindingResult.getFieldErrors());

        // 요청 날짜 기반으로 회고 삭제
        retrospectServiceV2.deleteRetro(user.getSocialId(), reqDeleteRetroDto);

        return new CommonResponse<>(ErrorCode.SUCCESS);
    }

}
