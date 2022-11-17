package com.nanal.backend.config.exception.customexception;

import com.nanal.backend.config.exception.CustomException;

public class DiaryNotFoundException extends CustomException {
    public DiaryNotFoundException(String message) {
        super(message);
    }
}
