package com.zima.zimasocial.context.communication.domain.repository;

import com.zima.zimasocial.context.communication.domain.entity.RecipientDomain;
import com.zima.zimasocial.context.communication.domain.value.RecipientId;

import java.util.Optional;

public interface RecipientDomainRepository {
    Optional<RecipientDomain> findByRecipientId(RecipientId id);
    Optional<RecipientDomain> findByRecipientIdWithSubscribers(RecipientId id);
    Optional<RecipientDomain> findBySlug(String slug);
    RecipientDomain getAuthenticatedRecipient();
    void save(RecipientDomain recipient);
}
