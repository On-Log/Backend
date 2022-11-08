package com.nanal.backend.config.exception;

import com.nanal.backend.config.response.CommonResponse;
import com.nanal.backend.config.response.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(BindException.class)
    public CommonResponse<?> processValidationError(HttpServletResponse response, BindException exception) throws IOException {
        log.error("BindingException 발생", exception);
        return new CommonResponse<>(ErrorCode.INVALID_INPUT_VALUE);
    }

    @ExceptionHandler(CustomException.class)
    public CommonResponse<?> processNotFoundError(CustomException exception) {
        log.error(exception.getMessage(), exception);
        return new CommonResponse<>(ErrorCode.INVALID_REQUEST);
    }
}