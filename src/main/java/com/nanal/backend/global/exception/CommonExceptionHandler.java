package com.nanal.backend.global.exception;

import com.nanal.backend.domain.mypage.exception.ChangeRetrospectDateException;
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
    public CommonResponse<?> inputValueInvalidError(HttpServletResponse response, MethodArgumentNotValidException e) {
        response.setStatus(ErrorCode.INVALID_INPUT_VALUE.getCode());
        log.error("[{}][{}] {}", AuthenticationUtil.getCurrentUserEmail(),e.getClass().getSimpleName(), e.getMessage());

        List<String> errorMessages = e.getBindingResult().getAllErrors().stream()
                .map(objectError -> objectError.getDefaultMessage())
                .collect(Collectors.toList());

        return new CommonResponse<>(ErrorCode.INVALID_INPUT_VALUE, errorMessages);
    }

    /**
     *  잘못된 요청 형식
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public CommonResponse<?> badRequestError(HttpServletResponse response, HttpMessageNotReadableException e) {
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


    /**
     *  사용자 정보 관련 예외
     */
    @ExceptionHandler(ChangeRetrospectDateException.class)
    public CommonResponse<?> remainRetrospectChange(HttpServletResponse response, ChangeRetrospectDateException e) {
        response.setStatus(ErrorCode.RETROSPECT_DATE_CHANGE_IMPOSSIBLE.getCode());
        log.error("[{}][{}] {}", AuthenticationUtil.getCurrentUserEmail(),e.getClass().getSimpleName(), e.getMessage());
        return new CommonResponse<>(ErrorCode.RETROSPECT_DATE_CHANGE_IMPOSSIBLE, e.getChangeableDate());
    }

    /**
     *  서버 에러
     */
    //@ExceptionHandler(Exception.class)
    public CommonResponse<?> internalServerErrorHandler(HttpServletResponse response, Exception e) {
        response.setStatus(ErrorCode.INTERNAL_SERVER_ERROR.getCode());
        log.error("[{}][{}] {}", AuthenticationUtil.getCurrentUserEmail(),e.getClass().getSimpleName(), ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        return new CommonResponse<>(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}