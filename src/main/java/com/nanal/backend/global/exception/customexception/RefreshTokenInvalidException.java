package com.nanal.backend.global.exception.customexception;

import com.nanal.backend.global.exception.CustomException;

public class RefreshTokenInvalidException extends CustomException {
    public RefreshTokenInvalidException(String message) {
        super(message);
    }
}
