package com.zima.zimasocial.context.account.service;

import com.zima.zimasocial.context.account.entity.AccountId;
import com.zima.zimasocial.context.account.infastructure.entity.RefreshTokenEntity;
import com.zima.zimasocial.context.account.infastructure.repository.RefreshTokenJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountValidator {
    private final RefreshTokenJpaRepository repository;
    public boolean hasAccessToApp(AccountId accountId) {
        Optional<RefreshTokenEntity> validToken = repository.findFirstByUserIdAndRevokedIsFalseAndExpiresAtGreaterThanOrderByCreatedAtDesc(accountId.getValue(), OffsetDateTime.now());
        return validToken.isPresent();
    }
}
