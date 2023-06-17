package com.nanal.backend.global.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 내부에서 문제가 발생했습니다."),

    // Success
    SUCCESS(true, HttpStatus.OK.value(), "요청에 성공하였습니다."),
    SUCCESS_BUT(false, HttpStatus.OK.value(), "요청에 성공하였습니다."),


    // 특정 정보를 권한이 없는 유저가 요청하거나, 존재하지 않는 정보를 요청할 때.
    BAD_REQUEST(false, 400, "잘못된 요청입니다."),
    FORBIDDEN(false, 403, "해당 요청에 대한 권한이 존재하지 않습니다."),

    TOO_MANY_REQUEST(false,410, "잠시후에 다시 요청해주세요."),
    TOO_MANY_DUPLICATED_REQUEST(false,411, "잠시후에 다시 요청해주세요."),

    // Validation
    INVALID_INPUT_VALUE(false, 455, "잘못된 입력값입니다."),

    // Token
    // Token 이 유효하지 않을 때.
    INVALID_TOKEN(false, 456, "유효하지 않은 Token 입니다."),
    // RefreshToken 이 유효하지 않을 때.
    INVALID_REFRESH_TOKEN(false, 457, "유효하지 않은 RefreshToken 입니다."),

    // Auth
    MEMBER_NOT_FOUND(false, 460, "존재하지 않는 사용자입니다."),
    EMAIL_ALREADY_EXIST(false, 461, "이미 존재하는 Email 입니다."),
    ACCOUNT_NOT_EXIST(false, 462, "존재하지 않는 계정입니다."),
    PASSWORD_INCORRECT(false, 463, "잘못된 비밀번호 입니다."),
    INVALID_CONFIRM_VALUE(false, 464, "잘못된 인증값 입니다."),
    ACCOUNT_ALREADY_EXIST(false, 465, "이미 다른 채널로 가입된 이메일 입니다."),

    // Diary
    // 일기 조회시 해당 날짜에 일기가 존재하지 않을 때.
    DIARY_NOT_FOUND(false, 470, "요청한 날짜에 작성된 일기가 없습니다."),
    // 일기 저장시 해당 날짜에 일기가 이미 존재할 때.
    DIARY_ALREADY_EXIST(false, 471, "이미 요청한 날짜에 작성한 일기가 존재합니다."),
    // 회고가 이미 작성되어 있을 때.
    RETROSPECT_ALREADY_WRITTEN(false,472,"이미 회고가 작성되어 일기 작성이 불가능합니다."),
    // 작성날짜가 일기 작성 주간이 아닐 때.
    NOT_IN_WRITABLE_DATE(false,473,"현재 일기작성주간에 작성가능한 날짜가 아닙니다."),
    // 일기 수정이 불가능할 때
    DIARY_CHANGE_UNAVAILABLE(false,475,"일기 수정이 불가능합니다."),

    // MyPage
    // 중복된 회고일을 입력할 때.
    RETROSPECT_DAY_DUPLICATION(false, 480, "중복된 회고일입니다."),
    // 회고일 변경으로부터 한 달이 지나지 않아 변경할 수 없을 때.
    RETROSPECT_DATE_CHANGE_IMPOSSIBLE(false, 481, "30일이 지나지 않아 회고일 변경이 불가능합니다."),

    // Retrospect
    RETROSPECT_NOT_FOUND(false, 490, "조회하고자 하는 회고가 존재하지 않습니다."),
    RETROSPECT_ALREADY_EXIST(false,491,"이미 요청한 날짜에 작성한 회고가 존재합니다."),
    RETROSPECT_ALL_DONE(false,492,"이번 달에 작성할 수 있는 모든 회고를 작성했습니다."),
    RETROSPECT_TIME_DONE(false,493,"회고 작성 및 수정은 회고일 당일 11시 59분까지만 가능합니다"),
    GOAL_NOT_FOUND(false,494,"해당 회고 목적은 존재하지 않습니다.");


    private Boolean isSuccess;
    private int code;
    private String message;

    ErrorCode(Boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}