package com.zima.zimasocial.context.account.exception;

import com.zima.zimasocial.shared.exception.ConflictException;

public class AccountIsAlreadyPrivateException extends ConflictException {
    public AccountIsAlreadyPrivateException() {
        super("account_already_private", "Account is already private");
    }
}
