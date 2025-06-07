package com.zimaberlin.zimasocial.context.account.repository;

import com.zimaberlin.zimasocial.context.account.value.CreateAccount;
import com.zimaberlin.zimasocial.context.account.entity.Account;

import java.util.Optional;

public interface AccountRepository {
    Account getAuthenticatedAccount();
    Optional<Account> findByEmailAndAuthProvider(String email, String provider);
    void save(Account account);
    Account createNewAccount(CreateAccount createAccount);
    Account findByUserId(Long userId);
}
