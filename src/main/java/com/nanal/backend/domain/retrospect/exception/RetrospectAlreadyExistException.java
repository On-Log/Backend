package com.nanal.backend.domain.retrospect.exception;

import com.nanal.backend.global.exception.CustomException;

public class RetrospectAlreadyExistException extends CustomException {
    public RetrospectAlreadyExistException(String message) { super(message); }
}
