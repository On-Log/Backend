package com.nanal.backend.domain.retrospect.exception;

import com.nanal.backend.global.exception.NanalException;
import com.nanal.backend.global.response.ErrorCode;

public class RetrospectAlreadyExistException extends NanalException {

    public static final NanalException EXCEPTION = new RetrospectAlreadyExistException();

    private RetrospectAlreadyExistException() {
        super(ErrorCode.RETROSPECT_ALREADY_EXIST);
    }
}
