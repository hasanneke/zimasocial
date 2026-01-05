package com.zima.zimasocial.context.account.exception;

import com.zima.zimasocial.exception.ConflictException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyFollowed extends ConflictException {
    private final String code;
    public UserAlreadyFollowed(String code){
        super("User is already followed");
        this.code = code;

    }
    public UserAlreadyFollowed() {
        super("User is already followed");
        this.code = "user_already_followed";
    }
}
