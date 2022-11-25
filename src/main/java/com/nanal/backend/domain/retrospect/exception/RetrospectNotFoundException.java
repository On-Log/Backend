package com.nanal.backend.domain.retrospect.exception;

import com.nanal.backend.global.exception.CustomException;

public class RetrospectNotFoundException extends CustomException {
    public RetrospectNotFoundException(String message) {
        super(message);
    }
}
