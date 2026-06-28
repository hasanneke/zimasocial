package com.zima.zimasocial.context.account.exception;

import com.zima.zimasocial.context.account.value.TokenResponse;
import com.zima.zimasocial.shared.exception.ConflictException;
import lombok.Getter;

@Getter
public class AccountDeletedException extends ConflictException {
    public AccountDeletedException(TokenResponse token) {
        super("account_marked_for_deletion", "Account marked for deletion", token);
    }
}
