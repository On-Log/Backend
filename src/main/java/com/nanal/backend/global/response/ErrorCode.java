package com.nanal.backend.global.response;

import lombok.Getter;
import org.apache.catalina.connector.Response;

@Getter
public enum ErrorCode {

    // Success
    SUCCESS(true, Response.SC_OK, "요청에 성공하였습니다."),


    INVALID_INPUT_VALUE(false, 4000, "잘못된 입력값입니다."),

    // Token
    // 토큰이 없거나 유효하지 않은 상태에서 정보를 요청할 때.
    INVALID_JWT(false, 4001, "토큰이 없거나 유효하지 않습니다. 로그인을 해주세요."),
    // 특정 정보를 권한이 없는 유저가 요청하거나, 존재하지 않는 정보를 요청할 때.
    INVALID_REQUEST(false, 4002, "잘못된 요청입니다."),
    // Token 이 유효하지 않을 때.
    INVALID_TOKEN(false, 4003, "유효하지 않은 Token 입니다."),
    // RefreshToken 이 유효하지 않을 때.
    INVALID_REFRESH_TOKEN(false, 4004, "유효하지 않은 RefreshToken 입니다."),

    // Auth
    MEMBER_NOT_FOUND(false, 4013, "존재하지 않는 사용자입니다."),

    // Diary
    DIARY_NOT_FOUND(false, 4024, "해당 날짜에 작성된 일기가 없습니다."),

    // Retrospect


    // MyPage
    RETROSPECTDAY_DUPLICATION(false, 4031, "중복된 회고일입니다."),
    // 중복된 회고일을 입력할 때.
    RESETAVAIL_FALSE(false, 4032, "지금은 회고일을 변경 할 수 없습니다.");
    //resetavail이 false일 때. (= 회고일 변경으로부터 한 달이 지나지 않아 변경할 수 없을 때.)


    private Boolean isSuccess;
    private int code;
    private String message;

    ErrorCode(Boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}