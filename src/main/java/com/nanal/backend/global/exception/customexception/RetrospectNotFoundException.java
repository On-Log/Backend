package com.nanal.backend.global.exception.customexception;

import com.nanal.backend.global.exception.CustomException;

public class RetrospectNotFoundException extends CustomException {
    public RetrospectNotFoundException(String message) {
        super(message);
    }
}
