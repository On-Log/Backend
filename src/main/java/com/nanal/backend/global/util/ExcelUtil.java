package com.nanal.backend.global.util;

import javax.servlet.http.HttpServletResponse;

public class ExcelUtil {
    // 엑셀 다운로드를 위한 헤더 설정 기능 추가
    public static void setExcelResponseHeaders(HttpServletResponse response, String filename) {
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + ".xlsx\"");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }
}
