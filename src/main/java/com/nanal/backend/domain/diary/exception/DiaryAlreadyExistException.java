package com.nanal.backend.domain.diary.exception;

import com.nanal.backend.global.exception.CustomException;

public class DiaryAlreadyExistException extends CustomException {
    public DiaryAlreadyExistException(String message) {
        super(message);
    }
}
