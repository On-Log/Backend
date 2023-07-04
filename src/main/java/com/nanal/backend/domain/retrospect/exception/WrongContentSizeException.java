package com.nanal.backend.domain.retrospect.exception;

import com.nanal.backend.global.exception.customexception.NanalException;
import com.nanal.backend.global.response.ErrorCode;

public class WrongContentSizeException extends NanalException {

    public static final NanalException EXCEPTION = new WrongContentSizeException();

    private WrongContentSizeException() {
        super(ErrorCode.WRONG_CONTENT_SIZE);
    }
}

