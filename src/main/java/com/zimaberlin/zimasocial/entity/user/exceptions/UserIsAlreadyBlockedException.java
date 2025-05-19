package com.zimaberlin.zimasocial.entity.user.exceptions;

import com.zimaberlin.zimasocial.exception.ConflictException;

public class UserIsAlreadyBlockedException extends ConflictException {
    public UserIsAlreadyBlockedException() {
        super("User is already blocked");
    }
}
