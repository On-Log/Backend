package com.nanal.backend.global.throttling;

import com.nanal.backend.global.exception.customexception.NanalException;
import com.nanal.backend.global.response.ErrorCode;

public class TooManyRequestException extends NanalException {

    public static final NanalException EXCEPTION = new TooManyRequestException();

    private TooManyRequestException() {
        super(ErrorCode.TOO_MANY_REQUEST);
    }
}