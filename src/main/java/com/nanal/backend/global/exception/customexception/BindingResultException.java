package com.nanal.backend.global.exception.customexception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.List;

@AllArgsConstructor
@Getter
public class BindingResultException extends RuntimeException{

    private List<FieldError> fieldErrors;
}
