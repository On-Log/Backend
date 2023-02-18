package com.nanal.backend.domain.auth.exception;

import com.nanal.backend.global.exception.customexception.NanalAuthException;
import com.nanal.backend.global.response.ErrorCode;

public class AccountAlreadyExistException extends NanalAuthException {
    public static final NanalAuthException EXCEPTION = new AccountAlreadyExistException();

    private AccountAlreadyExistException() {
        super(ErrorCode.ACCOUNT_ALREADY_EXIST);
    }
}
