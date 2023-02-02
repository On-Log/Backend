package com.nanal.backend.domain.search.controller;

import com.nanal.backend.domain.search.dto.ReqSearchDto;
import com.nanal.backend.domain.search.dto.RespSearchDto;
import com.nanal.backend.domain.search.service.SearchService;
import com.nanal.backend.global.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/search")
    public CommonResponse<?> search(@Valid ReqSearchDto reqSearchDto) {

        RespSearchDto respSearchDto = searchService.search(reqSearchDto);

        return new CommonResponse<>(respSearchDto);
    }
}
