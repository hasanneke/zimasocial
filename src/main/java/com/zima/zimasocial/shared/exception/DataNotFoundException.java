package com.zima.zimasocial.shared.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
public class DataNotFoundException extends RuntimeException {
    final String code;
    public DataNotFoundException(String code, String message) {
        super(message);
        this.code = code;
    }
    public DataNotFoundException(String message) {
        super(message);
        this.code = "not_found";
    }
}
