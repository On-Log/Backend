package com.nanal.backend.domain.auth.exception;

import com.nanal.backend.global.exception.customexception.NanalAuthException;
import com.nanal.backend.global.response.ErrorCode;

public class RefreshTokenInvalidException extends NanalAuthException {

    public static final NanalAuthException EXCEPTION = new RefreshTokenInvalidException();

    private RefreshTokenInvalidException() {
        super(ErrorCode.INVALID_REFRESH_TOKEN);
    }
}
