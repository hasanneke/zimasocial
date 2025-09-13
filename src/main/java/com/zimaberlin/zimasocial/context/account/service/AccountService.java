package com.zimaberlin.zimasocial.context.account.service;

import com.zimaberlin.zimasocial.context.account.value.CreateAccount;
import com.zimaberlin.zimasocial.context.account.entity.Account;
import com.zimaberlin.zimasocial.context.account.repository.AccountRepository;
import com.zimaberlin.zimasocial.context.account.value.DeleteReason;
import com.zimaberlin.zimasocial.context.account.value.DisableReason;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void disableAccount(DisableReason reason) {
        Account account = accountRepository.getAuthenticatedAccount();
        account.disableAccount(reason);
        accountRepository.save(account);
    }
    @Transactional
    public void activateAccount(Account account) {
        account.activateAccount();
        accountRepository.save(account);
    }
    @Transactional
    public void deleteAccount(DeleteReason reason) {
        Account account = accountRepository.getAuthenticatedAccount();
        account.deleteAccount(reason);
        accountRepository.save(account);
    }
    @Transactional
    public void makeAccountPublic(){
        Account account = accountRepository.getAuthenticatedAccount();
        account.makeAccountPublic();
        accountRepository.save(account);
    }
    @Transactional
    public void makeAccountPrivate(){
        Account account = accountRepository.getAuthenticatedAccount();
        account.makeAccountPrivate();
        accountRepository.save(account);
    }
    @Transactional
    public Account createAccount(Account account) {
        return accountRepository.createNewAccount(account);
    }
}
