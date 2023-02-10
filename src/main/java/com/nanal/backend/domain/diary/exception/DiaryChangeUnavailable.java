package com.nanal.backend.domain.diary.exception;

import com.nanal.backend.global.exception.customexception.NanalException;
import com.nanal.backend.global.response.ErrorCode;

public class DiaryChangeUnavailable extends NanalException {

    public static final NanalException EXCEPTION = new DiaryChangeUnavailable();

    private DiaryChangeUnavailable() {
        super(ErrorCode.DIARY_CHANGE_UNAVAILABLE);
    }
}
