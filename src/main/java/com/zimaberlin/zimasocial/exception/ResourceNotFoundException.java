package com.zimaberlin.zimasocial.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
public class ResourceNotFoundException extends RuntimeException {
    final String code;
    public ResourceNotFoundException(String code, String message) {
        super(message);
        this.code = code;
    }
    public ResourceNotFoundException(String message) {
        super(message);
        this.code = "not_found";
    }
}
