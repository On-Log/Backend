package com.nanal.backend.domain.sponsor.controller;

import com.nanal.backend.domain.sponsor.dto.req.ReqCheckSponsorDto;
import com.nanal.backend.domain.sponsor.service.SponsorService;
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
public class SponsorController {

    private final SponsorService sponsorService;

    /**
     * 후원자 인증
     * [POST] /sponsor
     * 작성자 : 장세은
     * 수정일 :
     */
    @PostMapping("/sponsor")
    public CommonResponse<?> checkSponsor(@AuthenticationPrincipal User user,
                                            @RequestBody @Valid ReqCheckSponsorDto reqCheckSponsorDto) {

        // 요청 날짜 기반으로 회고 기록
        sponsorService.checkSponsor(user.getSocialId(), reqCheckSponsorDto.getCode());

        return new CommonResponse<>(ErrorCode.SUCCESS);
    }
}
