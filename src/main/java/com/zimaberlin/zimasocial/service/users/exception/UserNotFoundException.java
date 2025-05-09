package com.zimaberlin.zimasocial.service.users.exception;

import com.zimaberlin.zimasocial.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends ResourceNotFoundException {

    public UserNotFoundException() {
        super("User not found");
    }
}
