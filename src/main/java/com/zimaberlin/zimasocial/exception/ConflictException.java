package com.zimaberlin.zimasocial.exception;

public class ConflictException extends RuntimeException{
    private String code;
    public ConflictException(String code,  String message) {
        super(message);
        this.code = code;
    }
    public ConflictException(String message){
        super(message);
    }
}
