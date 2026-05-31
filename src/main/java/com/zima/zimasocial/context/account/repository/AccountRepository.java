package com.zima.zimasocial.context.account.repository;

import com.zima.zimasocial.context.account.entity.Account;
import java.util.Optional;

public interface AccountRepository {
    Account getAuthenticatedAccount();
    Optional<Account> findByEmailAndAuthProvider(String email, String provider);
    void save(Account account);
    Account findByUserId(Long userId);
    Account findBySlug(String slug);
    Long nextId();
}
