package com.nanal.backend.domain.auth.exception;

import com.nanal.backend.global.exception.customexception.NanalAuthException;
import com.nanal.backend.global.response.ErrorCode;

public class PasswordIncorrectException extends NanalAuthException{
    public static final PasswordIncorrectException EXCEPTION = new PasswordIncorrectException();

    private PasswordIncorrectException() {
        super(ErrorCode.PASSWORD_INCORRECT);
    }
}
