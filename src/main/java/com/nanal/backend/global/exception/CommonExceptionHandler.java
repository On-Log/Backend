package com.nanal.backend.global.exception;

import com.nanal.backend.domain.auth.exception.RefreshTokenInvalidException;
import com.nanal.backend.domain.diary.exception.DiaryAlreadyExistException;
import com.nanal.backend.domain.diary.exception.DiaryNotFoundException;
import com.nanal.backend.domain.mypage.exception.ResetAvailException;
import com.nanal.backend.domain.mypage.exception.RetrospectDayDupException;
import com.nanal.backend.domain.retrospect.exception.RetrospectNotFoundException;
import com.nanal.backend.global.security.AuthenticationUtil;
import com.nanal.backend.global.exception.customexception.*;
import com.nanal.backend.global.response.CommonResponse;
import com.nanal.backend.global.response.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

    /**
     *  사용자 요청 관련 예외
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResponse<?> inputValueInvalidError(HttpServletResponse response, MethodArgumentNotValidException e) {
        response.setStatus(ErrorCode.INVALID_INPUT_VALUE.getCode());
        log.error("[{}][{}] {}", AuthenticationUtil.getCurrentUserEmail(),e.getClass().getSimpleName(), e.getMessage());

        List<String> errorMessages = e.getBindingResult().getAllErrors().stream()
                .map(objectError -> objectError.getDefaultMessage())
                .collect(Collectors.toList());

        return new CommonResponse<>(ErrorCode.INVALID_INPUT_VALUE, errorMessages);
    }

    /**
     *  인증 관련 예외
     */
    @ExceptionHandler(MemberAuthException.class)
    public CommonResponse<?> memberNotFoundError(HttpServletResponse response, MemberAuthException e) {
        response.setStatus(ErrorCode.MEMBER_NOT_FOUND.getCode());
        log.error("[{}][{}] {}", AuthenticationUtil.getCurrentUserEmail(),e.getClass().getSimpleName(), e.getMessage());
        return new CommonResponse<>(ErrorCode.MEMBER_NOT_FOUND);
    }

    @ExceptionHandler(RefreshTokenInvalidException.class)
    public CommonResponse<?> refreshTokenInvalidError(HttpServletResponse response, RefreshTokenInvalidException e) {
        response.setStatus(ErrorCode.INVALID_REFRESH_TOKEN.getCode());
        log.error("[{}][{}] {}", AuthenticationUtil.getCurrentUserEmail(),e.getClass().getSimpleName(), e.getMessage());
        return new CommonResponse<>(ErrorCode.INVALID_REFRESH_TOKEN);
    }

    /**
     *  일기 관련 예외
     */
    @ExceptionHandler(DiaryNotFoundException.class)
    public CommonResponse<?> diaryNotFoundError(HttpServletResponse response, DiaryNotFoundException e) {
        response.setStatus(ErrorCode.DIARY_NOT_FOUND.getCode());
        log.error("[{}][{}] {}", AuthenticationUtil.getCurrentUserEmail(),e.getClass().getSimpleName(), e.getMessage());
        return new CommonResponse<>(ErrorCode.DIARY_NOT_FOUND);
    }

    @ExceptionHandler(DiaryAlreadyExistException.class)
    public CommonResponse<?> diaryAlreadyExistError(HttpServletResponse response, DiaryAlreadyExistException e) {
        response.setStatus(ErrorCode.DIARY_ALREADY_EXIST.getCode());
        log.error("[{}][{}] {}", AuthenticationUtil.getCurrentUserEmail(),e.getClass().getSimpleName(), e.getMessage());
        return new CommonResponse<>(ErrorCode.DIARY_ALREADY_EXIST);
    }

    /**
     *  회고 관련 예외
     */
    @ExceptionHandler(RetrospectDayDupException.class)
    public CommonResponse<?> retrospectDayDupError(HttpServletResponse response, RetrospectDayDupException e) {
        response.setStatus(ErrorCode.RETROSPECT_DAY_DUPLICATION.getCode());
        log.error("[{}][{}] {}", AuthenticationUtil.getCurrentUserEmail(),e.getClass().getSimpleName(), e.getMessage());
        return new CommonResponse<>(ErrorCode.RETROSPECT_DAY_DUPLICATION);
    }

    @ExceptionHandler(RetrospectNotFoundException.class)
    public CommonResponse<?> retrospectNotFoundError(HttpServletResponse response, RetrospectNotFoundException e) {
        response.setStatus(ErrorCode.RETROSPECT_NOT_FOUND.getCode());
        log.error("[{}][{}] {}", AuthenticationUtil.getCurrentUserEmail(),e.getClass().getSimpleName(), e.getMessage());
        return new CommonResponse<>(ErrorCode.RETROSPECT_NOT_FOUND);
    }

    /**
     *  사용자 정보 관련 예외
     */
    @ExceptionHandler(ResetAvailException.class)
    public CommonResponse<?> resetAvailError(HttpServletResponse response, ResetAvailException e) {
        response.setStatus(ErrorCode.RESET_AVAIL_FALSE.getCode());
        log.error("[{}][{}] {}", AuthenticationUtil.getCurrentUserEmail(),e.getClass().getSimpleName(), e.getMessage());
        return new CommonResponse<>(ErrorCode.RESET_AVAIL_FALSE);
    }
}