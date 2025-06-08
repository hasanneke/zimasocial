package com.zimaberlin.zimasocial.context.account.exception;

import com.zimaberlin.zimasocial.exception.ConflictException;

public class AccountIsAlreadyPublicException extends ConflictException {
    public AccountIsAlreadyPublicException() {
        super("account_already_public", "Account is already public");
    }
}
