package com.zimaberlin.zimasocial.context.account.exception;

import com.zimaberlin.zimasocial.exception.ConflictException;

public class UserIsAlreadyBlockedException extends ConflictException {
    public UserIsAlreadyBlockedException() {
        super("User is already blocked");
    }
}
