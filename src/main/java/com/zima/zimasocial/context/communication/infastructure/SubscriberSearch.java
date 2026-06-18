package com.zima.zimasocial.context.communication.infastructure;

import com.zima.zimasocial.context.communication.domain.value.RecipientId;
import com.zima.zimasocial.context.communication.entity.Recipient;
import com.zima.zimasocial.context.communication.repository.RecipientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SubscriberSearch {
    private final RecipientRepository recipientRepository;
    public List<Recipient> findSubscribedRecipients(RecipientId parent) {
        return recipientRepository.findAllSubscribers(parent.getValue());
    }
}
