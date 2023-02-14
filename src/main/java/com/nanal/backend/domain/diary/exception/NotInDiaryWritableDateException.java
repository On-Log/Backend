package com.nanal.backend.domain.diary.exception;

import com.nanal.backend.global.exception.customexception.NanalException;
import com.nanal.backend.global.response.ErrorCode;

public class NotInDiaryWritableDateException extends NanalException {

    public static final NanalException EXCEPTION = new NotInDiaryWritableDateException();

    private NotInDiaryWritableDateException() {
        super(ErrorCode.NOT_IN_WRITABLE_DATE);
    }
}
