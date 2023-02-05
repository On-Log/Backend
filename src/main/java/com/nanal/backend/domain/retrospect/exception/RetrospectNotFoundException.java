package com.nanal.backend.domain.retrospect.exception;

import com.nanal.backend.global.exception.customexception.NanalException;
import com.nanal.backend.global.response.ErrorCode;

public class RetrospectNotFoundException extends NanalException {

    public static final NanalException EXCEPTION = new RetrospectNotFoundException();

    private RetrospectNotFoundException() {
        super(ErrorCode.RETROSPECT_NOT_FOUND);
    }
}
