package com.nanal.backend.global.exception.customexception;

public class InternalServerErrorException extends RuntimeException{

    public InternalServerErrorException(String message){ super(message); }
}
