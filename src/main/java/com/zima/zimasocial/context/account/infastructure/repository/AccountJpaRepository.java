package com.zima.zimasocial.context.account.infastructure.repository;

import com.zima.zimasocial.context.account.infastructure.adapter.AccountUserAdapter;
import com.zima.zimasocial.context.account.entity.Account;
import com.zima.zimasocial.context.account.repository.AccountRepository;
import com.zima.zimasocial.entity.user.UserEntity;
import com.zima.zimasocial.repository.UserJpaRepository;
import com.zima.zimasocial.service.users.exception.UserNotFoundException;
import com.zima.zimasocial.utility.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountJpaRepository implements AccountRepository {
    private final UserJpaRepository userRepository;
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
        Optional<UserEntity> user = userRepository.findById(account.getAccountId().getValue());
        if(user.isPresent()){
            user.get().mergeAccount(account);
            userRepository.save(user.get());
            return;
        }
        createNewAccount(account);
    }

    private Account createNewAccount(Account account) {
        UserEntity user = new UserEntity(
                account.getAccountId().getValue(),
                account.getEmail(), account.getName(),
                account.getFamilyName(),
                account.getAuthProvider(),
                account.getRoles(),
                account.getSlug());
        UserEntity createUserWithId = userRepository.save(user);
        return accountUserAdapter.convertUserEntityToAccount(createUserWithId);
    }

    @Override
    public Account findByUserId(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return accountUserAdapter.convertUserEntityToAccount(user);
    }

    @Override
    public Account findBySlug(String slug) {
        UserEntity user = userRepository.findBySlug(slug).orElseThrow(UserNotFoundException::new);
        return accountUserAdapter.convertUserEntityToAccount(user);
    }

    @Override
    public Long nextId() {
        return userRepository.nextId();
    }
}
