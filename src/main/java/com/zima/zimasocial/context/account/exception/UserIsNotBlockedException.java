package com.zima.zimasocial.context.account.exception;

import com.zima.zimasocial.shared.exception.ConflictException;

public class UserIsNotBlockedException extends ConflictException {
    public UserIsNotBlockedException() {
        super("User is not blocked");
    }
}
