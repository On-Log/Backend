package com.nanal.backend.domain.diary.exception;

import com.nanal.backend.global.exception.customexception.NanalException;
import com.nanal.backend.global.response.ErrorCode;

public class DiaryNotFoundException extends NanalException {

    public static final NanalException EXCEPTION = new DiaryNotFoundException();

    private DiaryNotFoundException() {
        super(ErrorCode.DIARY_NOT_FOUND);
    }
}
