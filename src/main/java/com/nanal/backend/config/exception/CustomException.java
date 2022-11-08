package com.nanal.backend.config.exception;


public class CustomException extends RuntimeException{

    private static final long serialVersionUID = 7693000823785675916L;

    public CustomException(String message) {
        super(message);
    }
}
