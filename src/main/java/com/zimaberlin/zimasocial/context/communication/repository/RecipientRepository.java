package com.zimaberlin.zimasocial.context.communication.repository;

import com.zimaberlin.zimasocial.context.communication.domain.Recipient;
import com.zimaberlin.zimasocial.context.communication.domain.RecipientId;

import java.util.Optional;

public interface RecipientRepository {
    Optional<Recipient> findByRecipientId(RecipientId id);
    Optional<Recipient> findBySlug(String slug);
    Recipient getAuthenticatedRecipient();
    void save(Recipient recipient);
}
