package com.nanal.backend.domain.mypage.exception;

import com.nanal.backend.global.exception.customexception.NanalException;
import com.nanal.backend.global.response.ErrorCode;
import lombok.Getter;


@Getter
public class ChangeRetrospectDateException extends NanalException {

    public static final NanalException EXCEPTION = new ChangeRetrospectDateException();

    private ChangeRetrospectDateException() {
        super(ErrorCode.RETROSPECT_DATE_CHANGE_IMPOSSIBLE);
    }
}
