package com.nanal.backend.domain.sponsor.exception;

import com.nanal.backend.global.exception.customexception.NanalException;
import com.nanal.backend.global.response.ErrorCode;

public class CodeAlreadyUsedException extends NanalException {

    public static final NanalException EXCEPTION = new CodeAlreadyUsedException();

    private CodeAlreadyUsedException() { super(ErrorCode.CODE_ALREADY_USED);}
}
