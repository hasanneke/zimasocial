package com.zima.zimasocial.context.account.service.handler;

import com.zima.zimasocial.context.account.entity.Account;
import com.zima.zimasocial.context.account.exception.AccountDeletedException;
import com.zima.zimasocial.context.account.service.JWTService;
import com.zima.zimasocial.context.account.service.handler.context.LoginContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountDeletedStep implements AccountStep {
    private final JWTService jwtService;
    @Override
    public void execute(LoginContext loginContext) throws Exception {
        if(loginContext.account().isEmpty()) return;
        Account account = loginContext.account().get();

        if(account.isMarkedForDeletion()) {
            throw new AccountDeletedException(jwtService.createRefreshToken(account));
        }
    }
}
