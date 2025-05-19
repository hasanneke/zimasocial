package com.zimaberlin.zimasocial.entity.user.exceptions;

import com.zimaberlin.zimasocial.exception.ConflictException;

public class UserIsNotBlockedException extends ConflictException {
    public UserIsNotBlockedException() {
        super("User is not blocked");
    }
}
