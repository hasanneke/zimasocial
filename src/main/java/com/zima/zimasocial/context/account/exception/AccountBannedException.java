package com.zima.zimasocial.context.account.exception;

import com.zima.zimasocial.exception.UnauthorizedException;

public class AccountBannedException extends UnauthorizedException {
    private final String code;
    public AccountBannedException() {
        super("Hesap Banlandı. Yanlış bir işlem olduğunu düşünüyorsanız iletişime geçiniz.");
        this.code = "account_banned";
    }
}
