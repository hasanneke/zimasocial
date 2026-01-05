package com.zima.zimasocial.service.users.exception;

import com.zima.zimasocial.exception.DataNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends DataNotFoundException {

    public UserNotFoundException() {
        super("User not found");
    }
}
