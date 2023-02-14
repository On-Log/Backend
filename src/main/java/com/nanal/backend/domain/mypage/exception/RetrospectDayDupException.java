package com.nanal.backend.domain.mypage.exception;

import com.nanal.backend.global.exception.customexception.NanalException;
import com.nanal.backend.global.response.ErrorCode;

public class RetrospectDayDupException extends NanalException {

    public static final NanalException EXCEPTION = new RetrospectDayDupException();

    private RetrospectDayDupException() {
        super(ErrorCode.RETROSPECT_DAY_DUPLICATION);
    }
}
