package com.nanal.backend.domain.analysis;


import com.nanal.backend.domain.analysis.dto.RespGetDauDto;
import com.nanal.backend.domain.analysis.dto.RespGetMauDto;
import com.nanal.backend.domain.analysis.dto.RespGetWauDto;
import com.nanal.backend.global.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AnalysisController {

    private final AnalysisService analysisService;

    /**
     * 일간 사용자 수
     * [GET] /analysis/dau
     * 작성자 : 장동호
     * 수정일 : 2022-11-21
     */
    @GetMapping("/analysis/dau")
    public CommonResponse<RespGetDauDto> getDau() {

        // 일간 사용자 수 조회
        RespGetDauDto respGetDauDto = analysisService.getDau();

        return new CommonResponse<>(respGetDauDto);
    }

    /**
     * 주간 사용자 수 - 일요일 기준
     * [GET] /analysis/wau
     * 작성자 : 장동호
     * 수정일 :
     */
    @GetMapping("/analysis/wau")
    public CommonResponse<RespGetWauDto> getWau() {

        RespGetWauDto respGetWauDto = analysisService.getWau();

        return new CommonResponse<>(respGetWauDto);
    }

    /**
     * 월간 사용자 수
     * [GET] /analysis/mau
     * 작성자 : 장동호
     * 수정일 :
     */
    @GetMapping("/analysis/mau")
    public CommonResponse<RespGetMauDto> getMau() {

        RespGetMauDto respGetMauDto = analysisService.getMau();

        return new CommonResponse<>(respGetMauDto);
    }

    /**
     * 각 요일별 [회고탭] 이용자 수
     * [GET] /analysis/retrospect
     * 작성자 : 장동호
     * 수정일 :
     */
    @GetMapping("/analysis/retrospect")
    public void getRetrospect() {

    }
}
