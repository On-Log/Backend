package com.nanal.backend.domain.auth.exception;

import com.nanal.backend.global.exception.customexception.NanalException;
import com.nanal.backend.global.response.ErrorCode;

public class RefreshTokenInvalidException extends NanalException {

    public static final NanalException EXCEPTION = new RefreshTokenInvalidException();

    private RefreshTokenInvalidException() {
        super(ErrorCode.INVALID_REFRESH_TOKEN);
    }
}
