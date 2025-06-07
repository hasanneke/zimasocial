package com.zimaberlin.zimasocial.context.account.infastructure.repository;

import com.zimaberlin.zimasocial.context.account.infastructure.adapter.AccountUserAdapter;
import com.zimaberlin.zimasocial.context.account.value.CreateAccount;
import com.zimaberlin.zimasocial.context.account.entity.Account;
import com.zimaberlin.zimasocial.context.account.repository.AccountRepository;
import com.zimaberlin.zimasocial.entity.user.UserEntity;
import com.zimaberlin.zimasocial.repository.UserRepository;
import com.zimaberlin.zimasocial.service.users.exception.UserNotFoundException;
import com.zimaberlin.zimasocial.utility.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountJpaRepository implements AccountRepository {
    private final UserRepository userRepository;
    private final AccountUserAdapter accountUserAdapter;
    @Override
    public Account getAuthenticatedAccount() {
        UserEntity user = CurrentUser.getCurrentUserProfile();
        return accountUserAdapter.convertUserEntityToAccount(user);
    }

    @Override
    public Optional<Account> findByEmailAndAuthProvider(String email, String provider) {
        UserEntity user = userRepository.findByEmailAndAuthProvider(email, provider).orElse(null);
        return Optional.ofNullable(accountUserAdapter.convertUserEntityToAccount(user));
    }

    @Override
    public void save(Account account) {
        UserEntity user = CurrentUser.getCurrentUserProfile();
        user.mergeAccount(account);
        userRepository.save(user);
    }

    @Override
    public Account createNewAccount(CreateAccount createAccount) {
        UserEntity user = new UserEntity(createAccount.email(), createAccount.name(), createAccount.familyName(), createAccount.authProvider(), createAccount.roles(), createAccount.slug());
        UserEntity createUserWithId = userRepository.save(user);
        return accountUserAdapter.convertUserEntityToAccount(createUserWithId);
    }

    @Override
    public Account findByUserId(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return accountUserAdapter.convertUserEntityToAccount(user);
    }
}
