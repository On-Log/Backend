package com.nanal.backend.domain.auth.exception;

import com.nanal.backend.global.exception.customexception.NanalAuthException;
import com.nanal.backend.global.response.ErrorCode;

public class AccountNotExistException extends NanalAuthException {
    public static final AccountNotExistException EXCEPTION = new AccountNotExistException();

    private AccountNotExistException() {
        super(ErrorCode.ACCOUNT_NOT_EXIST);
    }
}
