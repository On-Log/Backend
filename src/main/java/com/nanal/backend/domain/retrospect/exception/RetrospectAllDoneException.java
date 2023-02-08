package com.nanal.backend.domain.retrospect.exception;

import com.nanal.backend.global.exception.customexception.NanalException;
import com.nanal.backend.global.response.ErrorCode;

public class RetrospectAllDoneException extends NanalException {

    public static final NanalException EXCEPTION = new RetrospectAllDoneException();

    private RetrospectAllDoneException() {
        super(ErrorCode.RETROSPECT_ALL_DONE);
    }
}

