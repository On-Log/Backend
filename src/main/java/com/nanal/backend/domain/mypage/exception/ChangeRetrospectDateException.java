package com.nanal.backend.domain.mypage.exception;

import com.nanal.backend.global.exception.NanalException;
import com.nanal.backend.global.response.ErrorCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChangeRetrospectDateException extends NanalException {

    private LocalDateTime changeableDate;

    public ChangeRetrospectDateException(LocalDateTime changeableDate) {
        super(ErrorCode.RETROSPECT_DATE_CHANGE_IMPOSSIBLE);
        this.changeableDate = changeableDate;
    }
}
