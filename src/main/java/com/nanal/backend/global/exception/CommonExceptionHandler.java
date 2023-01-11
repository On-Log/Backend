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

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

    /**
     *  사용자 요청 관련 예외
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResponse<?> inputValueInvalidError(MethodArgumentNotValidException e) {
        log.error("[{}][{}] {}", AuthenticationUtil.getCurrentUserEmail(),e.getClass().getSimpleName(), e.getMessage());

        List<String> errorMessages = e.getBindingResult().getAllErrors().stream()
                .map(objectError -> objectError.getDefaultMessage())
                .collect(Collectors.toList());

        return new CommonResponse<>(ErrorCode.INVALID_INPUT_VALUE, errorMessages);
    }

    /**
     *  인증 관련 예외
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MemberAuthException.class)
    public CommonResponse<?> memberNotFoundError(MemberAuthException e) {
        log.error("[{}][{}] {}", AuthenticationUtil.getCurrentUserEmail(),e.getClass().getSimpleName(), e.getMessage());
        return new CommonResponse<>(ErrorCode.MEMBER_NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RefreshTokenInvalidException.class)
    public CommonResponse<?> refreshTokenInvalidError(RefreshTokenInvalidException e) {
        log.error("[{}][{}] {}", AuthenticationUtil.getCurrentUserEmail(),e.getClass().getSimpleName(), e.getMessage());
        return new CommonResponse<>(ErrorCode.INVALID_REFRESH_TOKEN);
    }

    /**
     *  일기 관련 예외
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DiaryNotFoundException.class)
    public CommonResponse<?> diaryNotFoundError(DiaryNotFoundException e) {
        log.error("[{}][{}] {}", AuthenticationUtil.getCurrentUserEmail(),e.getClass().getSimpleName(), e.getMessage());
        return new CommonResponse<>(ErrorCode.DIARY_NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DiaryAlreadyExistException.class)
    public CommonResponse<?> diaryAlreadyExistError(DiaryAlreadyExistException e) {
        log.error("[{}][{}] {}", AuthenticationUtil.getCurrentUserEmail(),e.getClass().getSimpleName(), e.getMessage());
        return new CommonResponse<>(ErrorCode.DIARY_ALREADY_EXIST);
    }

    /**
     *  회고 관련 예외
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RetrospectDayDupException.class)
    public CommonResponse<?> retrospectDayDupError(RetrospectDayDupException e) {
        log.error("[{}][{}] {}", AuthenticationUtil.getCurrentUserEmail(),e.getClass().getSimpleName(), e.getMessage());
        return new CommonResponse<>(ErrorCode.RETROSPECT_DAY_DUPLICATION);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RetrospectNotFoundException.class)
    public CommonResponse<?> retrospectNotFoundError(RetrospectNotFoundException e) {
        log.error("[{}][{}] {}", AuthenticationUtil.getCurrentUserEmail(),e.getClass().getSimpleName(), e.getMessage());
        return new CommonResponse<>(ErrorCode.RETROSPECT_NOT_FOUND);
    }

    /**
     *  사용자 정보 관련 예외
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ResetAvailException.class)
    public CommonResponse<?> resetAvailError(ResetAvailException e) {
        log.error("[{}][{}] {}", AuthenticationUtil.getCurrentUserEmail(),e.getClass().getSimpleName(), e.getMessage());
        return new CommonResponse<>(ErrorCode.RESET_AVAIL_FALSE);
    }


}