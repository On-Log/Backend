package com.nanal.backend.domain.mypage.exception;

import com.nanal.backend.global.exception.CustomException;

public class RetrospectDayDupException extends CustomException {
    public RetrospectDayDupException(String message) {
        super(message);
    }
}
