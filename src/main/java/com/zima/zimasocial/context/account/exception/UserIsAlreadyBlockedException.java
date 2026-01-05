package com.zima.zimasocial.context.account.exception;

import com.zima.zimasocial.exception.ConflictException;

public class UserIsAlreadyBlockedException extends ConflictException {
    public UserIsAlreadyBlockedException() {
        super("User is already blocked");
    }
}
