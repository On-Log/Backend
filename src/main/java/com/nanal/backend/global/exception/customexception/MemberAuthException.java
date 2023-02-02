package com.nanal.backend.global.exception.customexception;

import com.nanal.backend.global.exception.NanalException;
import com.nanal.backend.global.response.ErrorCode;

public class MemberAuthException extends NanalException {

    public static final NanalException EXCEPTION = new MemberAuthException();

    private MemberAuthException() {
        super(ErrorCode.MEMBER_NOT_FOUND);
    }
}
