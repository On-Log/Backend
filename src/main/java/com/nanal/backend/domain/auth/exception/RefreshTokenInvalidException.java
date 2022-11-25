package com.nanal.backend.domain.auth.exception;

import com.nanal.backend.global.exception.CustomException;

public class RefreshTokenInvalidException extends CustomException {
    public RefreshTokenInvalidException(String message) {
        super(message);
    }
}
