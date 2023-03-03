package com.nanal.backend.domain.retrospect.controller;

import com.nanal.backend.domain.retrospect.dto.req.*;
import com.nanal.backend.domain.retrospect.dto.resp.*;
import com.nanal.backend.domain.retrospect.service.RetrospectService;
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
public class RetrospectController {

    private final RetrospectService retrospectService;

    /**
     * 회고 탭 화면
     * [GET] /retrospect
     * 작성자 : 장세은
     * 수정일 :
     */
    @GetMapping("/retrospect")
    public CommonResponse<RespGetInfoDto> getInfo(@AuthenticationPrincipal User user,
                                                  @Valid ReqGetInfoDto reqGetInfoDto,
                                                  BindingResult bindingResult) {

        if(bindingResult.hasErrors()) throw new BindingResultException(bindingResult.getFieldErrors());

        // 요청 정보 기반으로 해당 날짜에 맞는 정보 조회
        RespGetInfoDto respGetInfoDto = retrospectService.getInfo(user.getSocialId(), reqGetInfoDto);

        return new CommonResponse<>(respGetInfoDto);
    }

    /**
     * 회고 기록
     * [POST] /retrospect
     * 작성자 : 장세은
     * 수정일 :
     */
    @PostMapping("/retrospect")
    public CommonResponse<?> saveRetrospect(@AuthenticationPrincipal User user,
                                            @RequestBody @Valid ReqSaveRetroDto reqSaveRetroDto) {

        // 요청 날짜 기반으로 회고 기록
        retrospectService.saveRetrospect(user.getSocialId(), reqSaveRetroDto);

        return new CommonResponse<>(ErrorCode.SUCCESS);
    }

    /**
     * 회고 조회
     * [GET] /retrospect/view
     * 작성자 : 장세은
     * 수정일 :
     */
    @GetMapping("/retrospect/view")
    public CommonResponse<RespGetRetroDto> getRetrospect(@AuthenticationPrincipal User user,
                                                         @Valid ReqGetRetroDto reqGetRetroDto,
                                                         BindingResult bindingResult) {

        if(bindingResult.hasErrors()) throw new BindingResultException(bindingResult.getFieldErrors());

        // 요청 날짜 기반으로 회고 조회
        RespGetRetroDto respGetRetroDto = retrospectService.getRetro(user.getSocialId(), reqGetRetroDto);

        return new CommonResponse<>(respGetRetroDto);
    }

    /**
     * 회고 수정
     * [PUT] /retrospect
     * 작성자 : 장세은
     * 수정일 :
     */
    @PutMapping("/retrospect")
    public CommonResponse<?> editRetrospect(@AuthenticationPrincipal User user,
                                            @RequestBody @Valid ReqEditRetroDto reqEditRetroDto) {

        // 요청 날짜 기반으로 회고 수정
        retrospectService.editRetrospect(user.getSocialId(), reqEditRetroDto);

        return new CommonResponse<>(ErrorCode.SUCCESS);
    }

    /**
     * 일기 작성 날짜+키워드+감정어 조회 (회고 시작하기 버튼에서 사용 됨)
     * [GET] /retrospect/keyword
     * 작성자 : 장세은
     * 수정일 :
     */
    @GetMapping("/retrospect/keyword")
    public CommonResponse<RespGetKeywordAndEmotionDto> getKeywordAndEmotion(@AuthenticationPrincipal User user) {


        // 일기 작성 날짜+키워드+감정어 조회
        RespGetKeywordAndEmotionDto respGetKeywordAndEmotionDto = retrospectService.getKeywordAndEmotion(user.getSocialId());

        return new CommonResponse<>(respGetKeywordAndEmotionDto);
    }

    /**
     * 회고 질문 + 도움말
     * [GET] /retrospect/question
     * 작성자 : 장세은
     * 수정일 :
     */
    @GetMapping("/retrospect/question")
    public CommonResponse<RespGetQuestionAndHelpDto> getQuestionAndHelp(@Valid ReqGetGoalDto reqGetGoalDto,
                                                                        BindingResult bindingResult) {

        if(bindingResult.hasErrors()) throw new BindingResultException(bindingResult.getFieldErrors());

        // 회고질문 + 도움말 조회
        RespGetQuestionAndHelpDto respGetQuestionAndHelp = retrospectService.getQuestionAndHelp(reqGetGoalDto);

        return new CommonResponse<>(respGetQuestionAndHelp);
    }

    /**
     * 추가 회고 질문 + 도움말
     * [GET] /retrospect/extra
     * 작성자 : 장세은
     * 수정일 :
     */
    @GetMapping("/retrospect/extra")
    public CommonResponse<RespGetExtraQuestionAndHelpDto> getExtraQuestionAndHelp(@AuthenticationPrincipal User user,
                                                                                  @Valid ReqGetGoalDto reqGetGoalDto,
                                                                                  BindingResult bindingResult) {

        if(bindingResult.hasErrors()) throw new BindingResultException(bindingResult.getFieldErrors());

        // 회고질문 + 도움말 조회
        RespGetExtraQuestionAndHelpDto respGetExtraQuestionAndHelp = retrospectService.getExtraQuestionAndHelp(user.getSocialId(), reqGetGoalDto);

        return new CommonResponse<>(respGetExtraQuestionAndHelp);
    }

    /**
     * 회고 존재 여부 체크 + 회고일 변경 이후 최초 회고인지
     * [GET] /retrospect/exist
     * 작성자 : 장세은
     * 수정일 :
     */
    @GetMapping("/retrospect/exist")
    public CommonResponse<?> checkExistRetrospect(@AuthenticationPrincipal User user) {

        RespCheckFirstRetrospect respCheckFirstRetrospect = retrospectService.checkFirstRetrospect(user.getSocialId());
        // 요청 날짜에 작성한 회고가 있는지 체크
        if (retrospectService.checkRetrospect(user.getSocialId()) == false)
            return new CommonResponse<>(respCheckFirstRetrospect);
        else
            return new CommonResponse<>(ErrorCode.SUCCESS_BUT);
    }

    /**
     * 회고 삭제
     * [DELETE] /retrospect
     */
    @DeleteMapping("/retrospect")
    public CommonResponse<?> deleteDiary(@AuthenticationPrincipal User user,
                                         @Valid @RequestBody ReqDeleteRetroDto reqDeleteRetroDto) {

        // 요청 날짜 기반으로 회고 삭제
        retrospectService.deleteRetro(user.getSocialId(), reqDeleteRetroDto);

        return new CommonResponse<>(ErrorCode.SUCCESS);
    }


}
