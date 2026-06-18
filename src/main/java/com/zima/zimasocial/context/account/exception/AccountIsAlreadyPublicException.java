package com.zima.zimasocial.context.account.exception;

import com.zima.zimasocial.shared.exception.ConflictException;

public class AccountIsAlreadyPublicException extends ConflictException {
    public AccountIsAlreadyPublicException() {
        super("account_already_public", "Account is already public");
    }
}
