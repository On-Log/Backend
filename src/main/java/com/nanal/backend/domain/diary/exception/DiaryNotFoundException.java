package com.nanal.backend.domain.diary.exception;

import com.nanal.backend.global.exception.CustomException;

public class DiaryNotFoundException extends CustomException {
    public DiaryNotFoundException(String message) {
        super(message);
    }
}
