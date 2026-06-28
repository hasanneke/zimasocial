package com.zima.zimasocial.context.account.service.handler;

import com.zima.zimasocial.context.account.entity.Account;
import com.zima.zimasocial.context.account.exception.AccountBannedException;
import com.zima.zimasocial.context.account.service.handler.context.LoginContext;
import org.springframework.stereotype.Component;

@Component
public class AccountBannedStep implements AccountStep {
    @Override
    public void execute(LoginContext loginContext) {
        if(loginContext.account().isEmpty()) return;
        Account account = loginContext.account().get();
        if(account.isBanned()) throw new AccountBannedException();
    }
}
