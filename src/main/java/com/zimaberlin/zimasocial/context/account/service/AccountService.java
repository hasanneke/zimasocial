package com.zimaberlin.zimasocial.context.account.service;

import com.zimaberlin.zimasocial.context.account.entity.Account;
import com.zimaberlin.zimasocial.context.account.repository.AccountRepository;
import com.zimaberlin.zimasocial.context.account.value.DeleteReason;
import com.zimaberlin.zimasocial.context.account.value.DisableReason;
import com.zimaberlin.zimasocial.utility.CurrentUser;
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
        Account account = accountRepository.findByUserId(CurrentUser.getCurrentUserProfile().getId());
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
        Account account = accountRepository.findByUserId(CurrentUser.getCurrentUserProfile().getId());
        account.deleteAccount(reason);
        accountRepository.save(account);
    }
    @Transactional
    public void makeAccountPublic(){
        Account account = accountRepository.findByUserId(CurrentUser.getCurrentUserProfile().getId());
        account.makeAccountPublic();
        accountRepository.save(account);
    }
    @Transactional
    public void makeAccountPrivate(){
        Account account = accountRepository.findByUserId(CurrentUser.getCurrentUserProfile().getId());
        account.makeAccountPrivate();
        accountRepository.save(account);
    }
    @Transactional
    public Account createAccount(Account account) {
        return accountRepository.createNewAccount(account);
    }

    public void acceptTermsOfUse() {
        Account account = accountRepository.findByUserId(CurrentUser.getCurrentUserProfile().getId());
        account.acceptTermsOfUse();
        accountRepository.save(account);
    }
}
