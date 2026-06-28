package com.zima.zimasocial.context.account.exception;

import com.zima.zimasocial.shared.exception.ConflictException;

public class AccountCannotBeDeletedException extends ConflictException {
    public AccountCannotBeDeletedException() {
        super("account_cannot_be_deleted", "Son hesap silme işleminin üzerinden bir hafta henüz geçmedi. Daha sonra tekrar deneyin.");
    }
}
