package com.nanal.backend.global.lock;

import com.nanal.backend.global.exception.customexception.NanalException;
import com.nanal.backend.global.response.ErrorCode;

public class DistributedLockException extends NanalException {
    public static final NanalException EXCEPTION = new DistributedLockException();

    private DistributedLockException() {
        super(ErrorCode.TOO_MANY_DUPLICATED_REQUEST);
    }
}
