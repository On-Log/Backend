package com.nanal.backend.global.exception.customexception;


import com.nanal.backend.global.response.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NanalException extends RuntimeException{

    private ErrorCode errorCode;
}
