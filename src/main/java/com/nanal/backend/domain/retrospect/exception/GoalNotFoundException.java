package com.nanal.backend.domain.retrospect.exception;

import com.nanal.backend.global.exception.customexception.NanalException;
import com.nanal.backend.global.response.ErrorCode;

public class GoalNotFoundException extends NanalException {

    public static final NanalException EXCEPTION = new GoalNotFoundException();

    private GoalNotFoundException() {
        super(ErrorCode.GOAL_NOT_FOUND);
    }
}