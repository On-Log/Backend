package com.nanal.backend.domain.excel.controller;

import com.nanal.backend.domain.excel.service.ExcelService;
import com.nanal.backend.global.util.ExcelUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ExcelController {

    private final ExcelService excelService;

    /**
     * 일간 사용자 수 (기획 분석용)
     * [GET] /excel/dau
     * 작성자 : 장세은
     * 수정일 : 2023-07-24
     */
    @GetMapping("/excel/dau")
    public void getDauByExcel(HttpServletResponse response) {
        // 일간 사용자 수 조회
        byte[] excelData = excelService.getDauByExcel();

        // 엑셀 다운로드를 위한 헤더 설정
        ExcelUtil.setExcelResponseHeaders(response, "dau_data");

        // 엑셀 파일 데이터를 response로 전송
        try (OutputStream out = response.getOutputStream()) {
            out.write(excelData);
        } catch (IOException e) {
            e.printStackTrace();
            // 예외 처리 로직 추가
        }
    }

    /**
     * 주간 사용자 수 (기획 분석용)
     * [GET] /excel/wau
     * 작성자 : 장세은
     * 수정일 : 2023-07-24
     */
    @GetMapping("/excel/wau")
    public void getWauByExcel(HttpServletResponse response) {
        // 일간 사용자 수 조회
        byte[] excelData = excelService.getWauByExcel();

        // 엑셀 다운로드를 위한 헤더 설정
        ExcelUtil.setExcelResponseHeaders(response, "wau_data");

        // 엑셀 파일 데이터를 response로 전송
        try (OutputStream out = response.getOutputStream()) {
            out.write(excelData);
        } catch (IOException e) {
            e.printStackTrace();
            // 예외 처리 로직 추가
        }
    }

}
