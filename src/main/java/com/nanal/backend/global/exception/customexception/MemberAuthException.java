package com.nanal.backend.global.exception.customexception;

import com.nanal.backend.global.exception.CustomException;

public class MemberAuthException extends CustomException {

    public MemberAuthException(String message) {
        super(message);
    }
}
