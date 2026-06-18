package com.zima.zimasocial.context.account.exception;

import com.zima.zimasocial.shared.exception.ConflictException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserNotFollowed extends ConflictException {
    private final String code;
    public UserNotFollowed(String code){
        super("User not followed");
        this.code = code;

    }
    public UserNotFollowed() {
        super("User not followed");
        this.code = "user_not_followed";
    }
}
