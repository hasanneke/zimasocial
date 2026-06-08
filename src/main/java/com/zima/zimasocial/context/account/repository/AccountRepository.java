package com.zima.zimasocial.context.account.repository;

import com.zima.zimasocial.context.account.entity.Account;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Optional;

public interface AccountRepository {
    Account getAuthenticatedAccount();
    Optional<Account> findByEmailAndAuthProvider(String email, String provider);
    void save(Account account);
    Account findByUserId(Long userId) throws AccountNotFoundException;
    Account findBySlug(String slug) throws AccountNotFoundException;
    Long nextId();
}
