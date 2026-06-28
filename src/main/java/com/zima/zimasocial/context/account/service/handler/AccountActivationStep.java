package com.zima.zimasocial.context.account.service.handler;

import com.zima.zimasocial.context.account.entity.Account;
import com.zima.zimasocial.context.account.repository.AccountRepository;
import com.zima.zimasocial.context.account.service.handler.context.LoginContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountActivationStep implements AccountStep{
    private final AccountRepository accountRepository;
    @Override
    public void execute(LoginContext loginContext) {
        if(loginContext.account().isEmpty()) return;
        Account account = loginContext.account().get();
        if(account.isDisabled()){
            account.activateAccount();
            accountRepository.save(account);
        }
    }
}
