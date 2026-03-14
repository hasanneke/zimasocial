package com.zima.zimasocial.service.auth.exception;

import com.zima.zimasocial.exception.UnauthorizedException;

public class AccountBannedException extends UnauthorizedException {
    private final String code;
    public AccountBannedException() {
        super("Hesap Banlandı. Yanlış bir işlem olduğunu düşünüyorsanız iletişime geçiniz.");
        this.code = "account_banned";
    }
}
