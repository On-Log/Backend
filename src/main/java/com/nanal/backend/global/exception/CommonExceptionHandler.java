package com.nanal.backend.global.exception;

import com.nanal.backend.global.exception.customexception.BindingResultException;
import com.nanal.backend.global.exception.customexception.NanalAuthException;
import com.nanal.backend.global.exception.customexception.NanalException;
import com.nanal.backend.global.security.AuthenticationUtil;
import com.nanal.backend.global.response.CommonResponse;
import com.nanal.backend.global.response.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
    public CommonResponse<?> inputValueInvalidExceptionHandler(HttpServletResponse response, MethodArgumentNotValidException e) {
        response.setStatus(ErrorCode.INVALID_INPUT_VALUE.getCode());

        e.getBindingResult().getAllErrors().stream()
                .forEach(o -> log.error("[{}][{}] {}", AuthenticationUtil.getCurrentUserEmail(),o.getClass().getSimpleName(), o.getDefaultMessage()));

        List<String> errorMessages = e.getBindingResult().getAllErrors().stream()
                .map(objectError -> objectError.getDefaultMessage())
                .collect(Collectors.toList());

        return new CommonResponse<>(ErrorCode.INVALID_INPUT_VALUE, errorMessages);
    }

    @ExceptionHandler(BindingResultException.class)
    public CommonResponse<?> bindingResultExceptionHandler(HttpServletResponse response, BindingResultException e) {
        response.setStatus(ErrorCode.INVALID_INPUT_VALUE.getCode());

        e.getFieldErrors().stream()
                .forEach(o -> log.error("[{}][{}] {}", AuthenticationUtil.getCurrentUserEmail(), o.getClass().getSimpleName(), o.getDefaultMessage()));

        List<String> errorMessages = e.getFieldErrors().stream()
                .map(objectError -> objectError.getDefaultMessage())
                .collect(Collectors.toList());

        return new CommonResponse<>(ErrorCode.INVALID_INPUT_VALUE, errorMessages);
    }

    /**
     *  잘못된 요청 형식
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public CommonResponse<?> badRequestErrorHandler(HttpServletResponse response, HttpMessageNotReadableException e) {
        response.setStatus(ErrorCode.BAD_REQUEST.getCode());
        log.error("[{}][{}] {}", AuthenticationUtil.getCurrentUserEmail(),e.getClass().getSimpleName(), e.getMessage());
        return new CommonResponse<>(ErrorCode.BAD_REQUEST);
    }


    /**
     *  공통 예외 처리
     */
    @ExceptionHandler(NanalException.class)
    public CommonResponse<?> nanalExceptionHandler(HttpServletResponse response, NanalException e) {
        response.setStatus(e.getErrorCode().getCode());
        log.error("[{}][{}] {}", AuthenticationUtil.getCurrentUserEmail(),e.getClass().getSimpleName(), e.getErrorCode().getMessage());
        return new CommonResponse<>(e.getErrorCode());
    }

    @ExceptionHandler(NanalAuthException.class)
    public CommonResponse<?> nanalExceptionHandler(HttpServletResponse response, NanalAuthException e) {
        response.setStatus(e.getErrorCode().getCode());
        log.error("[{}] {}", e.getClass().getSimpleName(), e.getErrorCode().getMessage());
        return new CommonResponse<>(e.getErrorCode());
    }

    /**
     *  서버 에러
     */
    @ExceptionHandler(Exception.class)
    public CommonResponse<?> internalServerErrorHandler(HttpServletResponse response, Exception e) {
        response.setStatus(ErrorCode.INTERNAL_SERVER_ERROR.getCode());
        log.error("[{}] {}", e.getClass().getSimpleName(), e.getMessage());
        return new CommonResponse<>(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}