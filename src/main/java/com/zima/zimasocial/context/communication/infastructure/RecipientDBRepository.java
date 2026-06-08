package com.zima.zimasocial.context.communication.infastructure;

import com.zima.zimasocial.context.communication.RecipientNotFoundException;
import com.zima.zimasocial.context.communication.domain.entity.RecipientDomain;
import com.zima.zimasocial.context.communication.domain.repository.RecipientDomainRepository;
import com.zima.zimasocial.context.communication.domain.value.RecipientId;
import com.zima.zimasocial.entity.user.UserEntity;
import com.zima.zimasocial.repository.UserDeviceTokeJpaRepository;
import com.zima.zimasocial.repository.UserJpaRepository;
import com.zima.zimasocial.utility.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RecipientDBRepository implements RecipientDomainRepository {
    private final UserJpaRepository userJpaRepository;
    private final UserDeviceTokeJpaRepository userDeviceTokeJpaRepository;
    @Override
    public Optional<RecipientDomain> findByRecipientId(RecipientId id) {
        Optional<UserEntity> user = userJpaRepository.findByIdWithDeviceTokensOpt(id.getValue());
        if(user.isPresent()){
            UserEntity userValue = user.get();
            return Optional.of(UserEntityRecipientConverter.toRecipient(userValue));
        }
        return Optional.empty();
    }
    @Override
    public Optional<RecipientDomain> findByRecipientIdWithSubscribers(RecipientId id) {
        Optional<UserEntity> user = userJpaRepository.findByIdWithDeviceTokensOpt(id.getValue());
        if(user.isPresent()){
            UserEntity userValue = user.get();
            return Optional.of(UserEntityRecipientConverter.toRecipient(userValue));
        }
        return Optional.empty();
    }

    @Override
    public Optional<RecipientDomain> findBySlug(String slug) {
        Optional<UserEntity> user = userJpaRepository.findBySlug(slug);
        if(user.isPresent()){
            UserEntity userValue = user.get();
            return Optional.of(UserEntityRecipientConverter.toRecipient(userValue));
        }
        return Optional.empty();
    }

    @Override
    public RecipientDomain getAuthenticatedRecipient() {
        UserEntity user = userJpaRepository.findByIdWithDeviceTokens(CurrentUser.getCurrentUserProfile().getId());
        return UserEntityRecipientConverter.toRecipient(user);
    }

    @Override
    public void save(RecipientDomain recipient) {
        UserEntity user = userJpaRepository.findById(recipient.getRecipientId().getValue()).orElseThrow(() -> new RecipientNotFoundException(recipient.getRecipientId()));
        userDeviceTokeJpaRepository.deleteAll(user.getDeviceTokens());
        user.mergeRecipient(recipient);
        userJpaRepository.save(user);
    }
}
