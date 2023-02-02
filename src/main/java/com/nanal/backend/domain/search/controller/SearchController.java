package com.nanal.backend.domain.search.controller;

import com.nanal.backend.domain.search.dto.ReqSearchDto;
import com.nanal.backend.domain.search.dto.RespSearchDto;
import com.nanal.backend.domain.search.service.SearchService;
import com.nanal.backend.global.exception.BindingResultException;
import com.nanal.backend.global.response.CommonResponse;
import com.nanal.backend.global.security.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/search")
    public CommonResponse<?> search(@AuthenticationPrincipal User user,
                                    @Valid ReqSearchDto reqSearchDto,
                                    BindingResult bindingResult) {

        if(bindingResult.hasErrors()) throw new BindingResultException(bindingResult.getFieldErrors());

        RespSearchDto respSearchDto = searchService.search(user.getSocialId(), reqSearchDto);

        return new CommonResponse<>(respSearchDto);
    }
}
