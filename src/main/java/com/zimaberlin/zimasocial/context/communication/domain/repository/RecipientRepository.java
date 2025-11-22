package com.zimaberlin.zimasocial.context.communication.domain.repository;

import com.zimaberlin.zimasocial.context.communication.domain.entity.Recipient;
import com.zimaberlin.zimasocial.context.communication.domain.value.RecipientId;

import java.util.Optional;

public interface RecipientRepository {
    Optional<Recipient> findByRecipientId(RecipientId id);
    Optional<Recipient> findBySlug(String slug);
    Recipient getAuthenticatedRecipient();
    void save(Recipient recipient);
}
