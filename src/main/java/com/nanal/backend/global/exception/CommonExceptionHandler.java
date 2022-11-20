package com.nanal.backend.global.exception;

import com.nanal.backend.global.exception.customexception.*;
import com.nanal.backend.global.response.CommonResponse;
import com.nanal.backend.global.response.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResponse<?> inputValueInvalidError(HttpServletResponse response, MethodArgumentNotValidException e) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        log.error("[" + e.getClass().getSimpleName() + "] " + e.getMessage());

        List<String> errorMessages = e.getBindingResult().getAllErrors().stream()
                .map(objectError -> objectError.getDefaultMessage())
                .collect(Collectors.toList());

        return new CommonResponse<>(ErrorCode.INVALID_INPUT_VALUE, errorMessages);
    }

    @ExceptionHandler(MemberAuthException.class)
    public CommonResponse<?> memberNotFoundError(HttpServletResponse response, MemberAuthException e) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        log.error("[" + e.getClass().getSimpleName() + "] " + e.getMessage());
        return new CommonResponse<>(ErrorCode.MEMBER_NOT_FOUND);
    }

    @ExceptionHandler(DiaryNotFoundException.class)
    public CommonResponse<?> diaryNotFoundError(HttpServletResponse response, DiaryNotFoundException e) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        log.error("[" + e.getClass().getSimpleName() + "] " + e.getMessage());
        return new CommonResponse<>(ErrorCode.DIARY_NOT_FOUND);
    }

    @ExceptionHandler(RetrospectDayDupException.class)
    public CommonResponse<?> retrospectDayDupError(HttpServletResponse response, RetrospectDayDupException e) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        log.error("[" + e.getClass().getSimpleName() + "] " + e.getMessage());
        return new CommonResponse<>(ErrorCode.RETROSPECTDAY_DUPLICATION);
    }

    @ExceptionHandler(ResetAvailException.class)
    public CommonResponse<?> resetAvailError(HttpServletResponse response, ResetAvailException e) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        log.error("[" + e.getClass().getSimpleName() + "] " + e.getMessage());
        return new CommonResponse<>(ErrorCode.RESETAVAIL_FALSE);
    }

    @ExceptionHandler(RefreshTokenInvalidException.class)
    public CommonResponse<?> refreshTokenInvalidError(HttpServletResponse response, RefreshTokenInvalidException e) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        log.error("[" + e.getClass().getSimpleName() + "] " + e.getMessage());
        return new CommonResponse<>(ErrorCode.INVALID_REFRESH_TOKEN);
    }
}