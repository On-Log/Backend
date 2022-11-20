package com.nanal.backend.global.exception.customexception;

import com.nanal.backend.global.exception.CustomException;
import lombok.Getter;
import org.springframework.validation.ObjectError;

import java.util.List;

@Getter
public class InputValueInvalidException extends CustomException {

    private List<ObjectError> objectErrors;

    public InputValueInvalidException(String message, List<ObjectError> objectErrors) {
        super(message);
        this.objectErrors = objectErrors;
    }
}
