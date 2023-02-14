package com.nanal.backend.domain.retrospect.exception;

import com.nanal.backend.global.exception.customexception.NanalException;
import com.nanal.backend.global.response.ErrorCode;

public class RetrospectTimeDoneException extends NanalException {

    public static final NanalException EXCEPTION = new RetrospectTimeDoneException();

    private RetrospectTimeDoneException() {
        super(ErrorCode.RETROSPECT_TIME_DONE);
    }
}
