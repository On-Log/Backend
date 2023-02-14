package com.nanal.backend.domain.diary.exception;

import com.nanal.backend.global.exception.customexception.NanalException;
import com.nanal.backend.global.response.ErrorCode;

public class DiaryAlreadyExistException extends NanalException {

    public static final NanalException EXCEPTION = new DiaryAlreadyExistException();

    private DiaryAlreadyExistException() {
        super(ErrorCode.DIARY_ALREADY_EXIST);
    }
}
