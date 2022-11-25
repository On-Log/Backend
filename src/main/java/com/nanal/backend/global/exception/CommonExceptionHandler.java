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
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResponse<?> inputValueInvalidError(HttpServletResponse response, MethodArgumentNotValidException e) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        log.error("[{}][{}] {}", AuthenticationUtil.getCurrentUserEmail(),e.getClass().getSimpleName(), e.getMessage());

        List<String> errorMessages = e.getBindingResult().getAllErrors().stream()
                .map(objectError -> objectError.getDefaultMessage())
                .collect(Collectors.toList());

        return new CommonResponse<>(ErrorCode.INVALID_INPUT_VALUE, errorMessages);
    }

    @ExceptionHandler(MemberAuthException.class)
    public CommonResponse<?> memberNotFoundError(HttpServletResponse response, MemberAuthException e) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        log.error("[{}][{}] {}", AuthenticationUtil.getCurrentUserEmail(),e.getClass().getSimpleName(), e.getMessage());
        return new CommonResponse<>(ErrorCode.MEMBER_NOT_FOUND);
    }

    @ExceptionHandler(DiaryNotFoundException.class)
    public CommonResponse<?> diaryNotFoundError(HttpServletResponse response, DiaryNotFoundException e) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        log.error("[{}][{}] {}", AuthenticationUtil.getCurrentUserEmail(),e.getClass().getSimpleName(), e.getMessage());
        return new CommonResponse<>(ErrorCode.DIARY_NOT_FOUND);
    }

    @ExceptionHandler(DiaryAlreadyExistException.class)
    public CommonResponse<?> diaryAlreadyExistError(HttpServletResponse response, DiaryAlreadyExistException e) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        log.error("[{}][{}] {}", AuthenticationUtil.getCurrentUserEmail(),e.getClass().getSimpleName(), e.getMessage());
        return new CommonResponse<>(ErrorCode.DIARY_ALREADY_EXIST);
    }

    @ExceptionHandler(RetrospectDayDupException.class)
    public CommonResponse<?> retrospectDayDupError(HttpServletResponse response, RetrospectDayDupException e) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        log.error("[{}][{}] {}", AuthenticationUtil.getCurrentUserEmail(),e.getClass().getSimpleName(), e.getMessage());
        return new CommonResponse<>(ErrorCode.RETROSPECT_DAY_DUPLICATION);
    }

    @ExceptionHandler(RetrospectNotFoundException.class)
    public CommonResponse<?> retrospectNotFoundError(HttpServletResponse response, RetrospectNotFoundException e) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        log.error("[{}][{}] {}", AuthenticationUtil.getCurrentUserEmail(),e.getClass().getSimpleName(), e.getMessage());
        return new CommonResponse<>(ErrorCode.RETROSPECT_NOT_FOUND);
    }

    @ExceptionHandler(ResetAvailException.class)
    public CommonResponse<?> resetAvailError(HttpServletResponse response, ResetAvailException e) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        log.error("[{}][{}] {}", AuthenticationUtil.getCurrentUserEmail(),e.getClass().getSimpleName(), e.getMessage());
        return new CommonResponse<>(ErrorCode.RESET_AVAIL_FALSE);
    }

    @ExceptionHandler(RefreshTokenInvalidException.class)
    public CommonResponse<?> refreshTokenInvalidError(HttpServletResponse response, RefreshTokenInvalidException e) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        log.error("[{}][{}] {}", AuthenticationUtil.getCurrentUserEmail(),e.getClass().getSimpleName(), e.getMessage());
        return new CommonResponse<>(ErrorCode.INVALID_REFRESH_TOKEN);
    }
}