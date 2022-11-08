package com.nanal.backend.config.response;

import lombok.Getter;
import org.apache.catalina.connector.Response;

@Getter
public enum ErrorCode {

    // Success
    SUCCESS(true, Response.SC_OK, "요청에 성공하였습니다."),

    // Token
    INVALID_INPUT_VALUE(false, 4000, "잘못된 입력값입니다."),
    // 토큰이 없거나 유효하지 않은 상태에서 정보를 요청할 때.
    INVALID_JWT(false, 4001, "토큰이 없거나, 유효하지 않습니다. 로그인을 해주세요."),
    // 특정 정보를 권한이 없는 유저가 요청하거나, 존재하지 않는 정보를 요청할 때.
    INVALID_REQUEST(false, 4002, "잘못된 요청입니다.");

    // Diary


    // Retrospect


    // MyPage


    private Boolean isSuccess;
    private int code;
    private String message;

    ErrorCode(Boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}