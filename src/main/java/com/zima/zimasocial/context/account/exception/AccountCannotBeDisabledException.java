package com.zima.zimasocial.context.account.exception;

import com.zima.zimasocial.shared.exception.ConflictException;

public class AccountCannotBeDisabledException extends ConflictException {
    public AccountCannotBeDisabledException() {
        super("account_cannot_be_disabled", "Son hesap devre dışı bırakma işleminin üzerinden bir hafta henüz geçmedi. Daha sonra tekrar deneyin.");
    }
}
