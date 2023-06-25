package com.nanal.backend.domain.sponsor.exception;

import com.nanal.backend.global.exception.customexception.NanalException;
import com.nanal.backend.global.response.ErrorCode;

public class CodeNotExistException extends NanalException {

    public static final NanalException EXCEPTION = new CodeNotExistException();

    private CodeNotExistException() {
        super(ErrorCode.RETROSPECT_TIME_DONE);
    }
}

