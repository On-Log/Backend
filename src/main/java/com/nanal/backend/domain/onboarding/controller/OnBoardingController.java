package com.nanal.backend.domain.onboarding.controller;

import com.nanal.backend.domain.onboarding.dto.ReqSetRetrospectDayDto;
import com.nanal.backend.domain.onboarding.service.OnBoardingService;
import com.nanal.backend.global.response.CommonResponse;
import com.nanal.backend.global.response.ErrorCode;
import com.nanal.backend.global.security.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class OnBoardingController {

    private final OnBoardingService onBoardingService;

    @PostMapping("/onBoarding")
    public CommonResponse<?> onBoarding(@AuthenticationPrincipal User user,
                                     @RequestBody @Valid ReqSetRetrospectDayDto reqSetRetrospectDayDto) {

        onBoardingService.setRetrospectDay(user.getSocialId(), reqSetRetrospectDayDto.getRetrospectDay());

        return new CommonResponse<>(ErrorCode.SUCCESS);
    }
}
