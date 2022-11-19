package com.nanal.backend.domain.retrospect.controller;

import com.nanal.backend.config.response.CommonResponse;
import com.nanal.backend.config.response.ErrorCode;
import com.nanal.backend.domain.oauth.UserDto;
import com.nanal.backend.domain.retrospect.dto.*;
import com.nanal.backend.domain.retrospect.service.RetrospectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@Tag(name = "RetrospectController", description = "회고 관련 api")

public class RetrospectController {

    private final RetrospectService retrospectService;

    /**
     * 회고 탭 화면
     * [GET] /retrospect
     * 작성자 : 장세은
     * 수정일 :
     */
    @Operation(summary="회고 탭 화면 조회", description="해당 날짜에 맞는 정보 조회")
    @GetMapping("/retrospect")
    public CommonResponse<RespGetInfoDto> getInfo(@Parameter @AuthenticationPrincipal UserDto userDto, @RequestBody ReqGetInfoDto reqGetInfoDto) {

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
    @Operation(summary="회고 정보 저장", description="해당 날짜에 맞는 정보 조회")
    @PostMapping("/retrospect")
    public CommonResponse<?> saveRetrospect(@Parameter @AuthenticationPrincipal UserDto userDto, @RequestBody @Valid ReqSaveRetroDto reqSaveRetroDto) {

        // 요청 날짜 기반으로 회고 기록
        retrospectService.saveRetrospect(userDto.getEmail(), reqSaveRetroDto);

        return new CommonResponse<>(ErrorCode.SUCCESS);
    }

    /**
     * 회고 조회
     * [GET] /retrospect/view
     * 작성자 : 장세은
     * 수정일 :
     */
    @Operation(summary="회고 정보 조회", description="요청 날짜 기반으로 회고 조회")
    @GetMapping("/retrospect/view")
    public CommonResponse<RespGetRetroDto> getRetrospect(@Parameter @AuthenticationPrincipal UserDto userDto, @RequestBody ReqGetRetroDto reqGetRetroDto) {

        // 요청 날짜 기반으로 회고 조회
        RespGetRetroDto respGetRetroDto = retrospectService.getRetro(userDto.getEmail(), reqGetRetroDto);

        return new CommonResponse<>(respGetRetroDto);
    }

    /**
     * 회고 수정
     * [PUT] /retrospect
     * 작성자 : 장세은
     * 수정일 :
     */
    @Operation(summary="회고 정보 수정", description="요청 날짜 기반으로 회고 수정")
    @PutMapping("/retrospect")
    public CommonResponse<?> editRetrospect(@Parameter @AuthenticationPrincipal UserDto userDto, @RequestBody @Valid ReqEditRetroDto reqEditRetroDto) {

        // 요청 날짜 기반으로 회고 수정
        retrospectService.editRetrospect(userDto.getEmail(), reqEditRetroDto);

        return new CommonResponse<>(ErrorCode.SUCCESS);
    }

    /**
     * 일기 작성 날짜+키워드+감정어 조회
     * [GET] /retrospect/keyword
     * 작성자 : 장세은
     * 수정일 :
     */
    @Operation(summary="일기 작성 날짜+키워드+감정어 조회")
    @GetMapping("/retrospect/keyword")
    public CommonResponse<RespGetKeywordAndEmotionDto> getKeywordAndEmotion(@Parameter @AuthenticationPrincipal UserDto userDto, @RequestBody ReqGetKeywordAndEmotionDto reqGetKeywordAndEmotionDto) {

        // 일기 작성 날짜+키워드+감정어 조회
        RespGetKeywordAndEmotionDto respGetKeywordAndEmotionDto = retrospectService.getKeywordAndEmotion(userDto.getEmail(), reqGetKeywordAndEmotionDto);

        return new CommonResponse<>(respGetKeywordAndEmotionDto);
    }

    /**
     * 회고 질문 + 도움말
     * [GET] /retrospect/question
     * 작성자 : 장세은
     * 수정일 :
     */
    @Operation(summary="회고 질문+도움말", description="회고질문+도움말 조회")
    @GetMapping("/retrospect/question")
    public CommonResponse<RespGetQuestionAndHelpDto> getQuestionAndHelp() {

        // 회고질문 + 도움말 조회
        RespGetQuestionAndHelpDto respGetQuestionAndHelp = retrospectService.getQuestionAndHelp();

        return new CommonResponse<>(respGetQuestionAndHelp);
    }

}
