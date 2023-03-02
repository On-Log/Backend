package com.nanal.backend.domain.auth.exception;

import com.nanal.backend.global.exception.customexception.NanalAuthException;
import com.nanal.backend.global.response.ErrorCode;

public class InvalidConfirmValueException extends NanalAuthException {
    public static final NanalAuthException EXCEPTION = new InvalidConfirmValueException();

    private InvalidConfirmValueException() {
        super(ErrorCode.INVALID_CONFIRM_VALUE);
    }
}
