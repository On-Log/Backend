package com.nanal.backend.domain.search.controller;

import com.nanal.backend.domain.search.dto.req.ReqSearchDto;
import com.nanal.backend.domain.search.dto.resp.RespSearchDiaryDto;
import com.nanal.backend.domain.search.dto.resp.RespSearchRetrospectDto;
import com.nanal.backend.domain.search.service.SearchService;
import com.nanal.backend.global.exception.customexception.BindingResultException;
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

    @GetMapping("/search/diary")
    public CommonResponse<?> searchDiary(@AuthenticationPrincipal User user,
                                    @Valid ReqSearchDto reqSearchDto,
                                    BindingResult bindingResult) {

        if(bindingResult.hasErrors()) throw new BindingResultException(bindingResult.getFieldErrors());


        RespSearchDiaryDto respSearchDiaryDto = searchService.searchDiary(user.getSocialId(), reqSearchDto);
        return new CommonResponse<>(respSearchDiaryDto);
    }

    @GetMapping("/search/retrospect")
    public CommonResponse<?> searchRetrospect(@AuthenticationPrincipal User user,
                                    @Valid ReqSearchDto reqSearchDto,
                                    BindingResult bindingResult) {

        if(bindingResult.hasErrors()) throw new BindingResultException(bindingResult.getFieldErrors());


        RespSearchRetrospectDto respSearchRetrospectDto = searchService.searchRetrospect(user.getSocialId(), reqSearchDto);
        return new CommonResponse<>(respSearchRetrospectDto);
    }
}
