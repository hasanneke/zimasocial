package com.zimaberlin.zimasocial.entity.user.exceptions;

import com.zimaberlin.zimasocial.exception.ConflictException;

public class AccountIsAlreadyPrivateException extends ConflictException {
    public AccountIsAlreadyPrivateException() {
        super("account_already_private", "Account is already private");
    }
}
