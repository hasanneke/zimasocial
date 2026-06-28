package com.zima.zimasocial.context.account.repository;

import com.zima.zimasocial.context.account.entity.Account;
import com.zima.zimasocial.context.account.entity.AccountId;
import com.zima.zimasocial.context.account.service.LoginType;
import com.zima.zimasocial.shared.CurrentUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, AccountId> {
    Optional<Account> findByEmailAndLoginType(String email, LoginType loginType);
    Optional<Account> findBySlug(String slug);
    Optional<Account> findByAccountId(AccountId accountId);
    @Query(value = "SELECT nextval('user_sequence')")
    Long nextId();

    default Account getAuthenticatedAccount() {
        return findByAccountId(new AccountId(CurrentUser.getCurrentUserId().getValue())).orElse(null);
    }

    @Query(value = "SELECT * FROM users WHERE slug = :slug AND IS_DELETED = FALSE AND IS_DISABLED = FALSE", nativeQuery = true)
    Optional<Account> findBySlugWithDeletedUsers(String slug);
}
