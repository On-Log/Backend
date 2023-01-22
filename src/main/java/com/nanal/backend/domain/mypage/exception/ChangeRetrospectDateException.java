package com.nanal.backend.domain.mypage.exception;

import com.nanal.backend.global.exception.CustomException;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChangeRetrospectDateException extends CustomException {

    private LocalDateTime changeableDate;

    public ChangeRetrospectDateException(String message, LocalDateTime changeableDate) {
        super(message);
        this.changeableDate = changeableDate;
    }
}
