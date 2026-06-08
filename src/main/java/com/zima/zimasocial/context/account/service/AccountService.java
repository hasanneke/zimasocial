package com.zima.zimasocial.context.account.service;
import com.zima.zimasocial.context.account.entity.Account;
import com.zima.zimasocial.context.account.repository.AccountRepository;
import com.zima.zimasocial.context.account.value.DeleteReason;
import com.zima.zimasocial.context.account.value.DisableReason;
import com.zima.zimasocial.utility.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void disableAccount(DisableReason reason) throws AccountNotFoundException {
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
    public void deleteAccount(DeleteReason reason) throws AccountNotFoundException {
        Account account = accountRepository.findByUserId(CurrentUser.getCurrentUserProfile().getId());
        account.deleteAccount(reason);
        accountRepository.save(account);
    }
    @Transactional
    public void makeAccountPublic() throws AccountNotFoundException {
        Account account = accountRepository.findByUserId(CurrentUser.getCurrentUserProfile().getId());
        account.makeAccountPublic();
        accountRepository.save(account);
    }
    @Transactional
    public void makeAccountPrivate() throws AccountNotFoundException {
        Account account = accountRepository.findByUserId(CurrentUser.getCurrentUserProfile().getId());
        account.makeAccountPrivate();
        accountRepository.save(account);
    }

    public void acceptTermsOfUse() throws AccountNotFoundException {
        Account account = accountRepository.findByUserId(CurrentUser.getCurrentUserProfile().getId());
        account.acceptTermsOfUse();
        accountRepository.save(account);
    }
}
