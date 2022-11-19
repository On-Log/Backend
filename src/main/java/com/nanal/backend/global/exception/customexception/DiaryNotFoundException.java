package com.nanal.backend.global.exception.customexception;

import com.nanal.backend.global.exception.CustomException;

public class DiaryNotFoundException extends CustomException {
    public DiaryNotFoundException(String message) {
        super(message);
    }
}
