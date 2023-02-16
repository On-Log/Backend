package com.nanal.backend.domain.auth.exception;

import com.nanal.backend.global.exception.customexception.NanalAuthException;
import com.nanal.backend.global.response.ErrorCode;

public class EmailAlreadyExistException extends NanalAuthException {
    public static final NanalAuthException EXCEPTION = new EmailAlreadyExistException();

    private EmailAlreadyExistException() {
        super(ErrorCode.EMAIL_ALREADY_EXIST);
    }
}
