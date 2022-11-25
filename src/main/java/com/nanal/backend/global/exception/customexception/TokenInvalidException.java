package com.nanal.backend.global.exception.customexception;

import com.nanal.backend.global.exception.CustomException;

public class TokenInvalidException extends CustomException {
    public TokenInvalidException(String message) {
        super(message);
    }
}
