package com.nanal.backend.domain.diary.exception;

import com.nanal.backend.global.exception.customexception.NanalException;
import com.nanal.backend.global.response.ErrorCode;

public class RetrospectAlreadyWrittenException extends NanalException{
    public static final NanalException EXCEPTION = new RetrospectAlreadyWrittenException();

    private RetrospectAlreadyWrittenException() {
        super(ErrorCode.RETROSPECT_ALREADY_WRITTEN);
    }
}
