package com.nanal.backend.global.exception.customexception;

import com.nanal.backend.global.response.ErrorCode;

public class TokenInvalidException extends NanalException {

    public static final NanalException EXCEPTION = new TokenInvalidException();

    private TokenInvalidException() {
        super(ErrorCode.INVALID_TOKEN);
    }
}
