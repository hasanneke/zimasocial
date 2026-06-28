package com.zima.zimasocial.shared.exception;

import lombok.Getter;

@Getter
public class ConflictException extends RuntimeException{
    private String code;
    private Object body;
    public ConflictException(String code, String message) {
        super(message);
        this.code = code;
    }
    public ConflictException(String code, String message, Object body) {
        super(message);
        this.code = code;
        this.body = body;
    }
    public ConflictException(String message){
        super(message);
    }

    public ConflictException(){
        super("conflict");
    }
}
