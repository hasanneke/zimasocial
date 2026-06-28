package com.zima.zimasocial.context.account.service.handler.context;

import com.zima.zimasocial.context.account.entity.Account;
import com.zima.zimasocial.context.account.service.LoginCredential;
import com.zima.zimasocial.context.account.service.UserInfo;
import com.zima.zimasocial.context.account.value.TokenResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

public class LoginContext {
    @Setter
    private Account account;
    @Setter
    @Getter
    private TokenResponse token;
    @Setter
    @Getter
    private UserInfo userInfo;
    @Getter
    private final LoginCredential loginCredential;

    public LoginContext(LoginCredential loginCredential) {
        this.loginCredential = loginCredential;
    }

    public Optional<Account> account() {
        return Optional.ofNullable(account);
    }
}
