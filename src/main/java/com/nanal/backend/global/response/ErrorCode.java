package com.nanal.backend.global.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 내부에서 문제가 발생했습니다."),

    // Success
    SUCCESS(true, HttpStatus.OK.value(), "요청에 성공하였습니다."),


    // 특정 정보를 권한이 없는 유저가 요청하거나, 존재하지 않는 정보를 요청할 때.
    BAD_REQUEST(false, 400, "잘못된 요청입니다."),

    // Validation
    INVALID_INPUT_VALUE(false, 455, "잘못된 입력값입니다."),

    // Token
    // Token 이 유효하지 않을 때.
    INVALID_TOKEN(false, 456, "유효하지 않은 Token 입니다."),
    // RefreshToken 이 유효하지 않을 때.
    INVALID_REFRESH_TOKEN(false, 457, "유효하지 않은 RefreshToken 입니다."),

    // Auth
    MEMBER_NOT_FOUND(false, 460, "존재하지 않는 사용자입니다."),

    // Diary
    // 일기 조회시 해당 날짜에 일기가 존재하지 않을 때.
    DIARY_NOT_FOUND(false, 470, "요청한 날짜에 작성된 일기가 없습니다."),
    // 일기 저장시 해당 날짜에 일기가 이미 존재할 때.
    DIARY_ALREADY_EXIST(false, 471, "이미 요청한 날짜에 작성한 일기가 존재합니다."),


    // MyPage
    // 중복된 회고일을 입력할 때.
    RETROSPECT_DAY_DUPLICATION(false, 480, "중복된 회고일입니다."),
    // resetavail이 false일 때. (= 회고일 변경으로부터 한 달이 지나지 않아 변경할 수 없을 때.)
    RESET_AVAIL_FALSE(false, 481, "이미 이번달에 회고일을 변경하였습니다."),

    // Retrospect
    RETROSPECT_NOT_FOUND(false, 490, "조회하고자 하는 회고가 존재하지 않습니다.");

    private Boolean isSuccess;
    private int code;
    private String message;

    ErrorCode(Boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}