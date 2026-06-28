package com.zima.zimasocial.context.account.service.handler;

import com.zima.zimasocial.context.account.entity.Account;
import com.zima.zimasocial.context.account.event.AccountCreatedEvent;
import com.zima.zimasocial.context.account.factory.AccountFactory;
import com.zima.zimasocial.context.account.repository.AccountRepository;
import com.zima.zimasocial.context.account.service.handler.context.LoginContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountCreationStep implements AccountStep {
    private final AccountFactory accountFactory;
    private final AccountRepository accountRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    @Override
    public void execute(LoginContext loginContext) throws Exception {
        if(loginContext.account().isPresent()) return;
        Account newAccount = accountFactory.createAccount(loginContext.getUserInfo(), loginContext.getLoginCredential().getLoginType());
        accountRepository.save(newAccount);
        loginContext.setAccount(newAccount);
        applicationEventPublisher.publishEvent(new AccountCreatedEvent(newAccount.getAccountId().getValue()));
    }
}
