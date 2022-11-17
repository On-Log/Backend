package com.nanal.backend.config.exception.customexception;

import com.nanal.backend.config.exception.CustomException;

public class MemberAuthException extends CustomException {

    public MemberAuthException(String message) {
        super(message);
    }
}
