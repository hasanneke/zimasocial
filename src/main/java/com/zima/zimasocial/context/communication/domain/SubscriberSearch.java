package com.zima.zimasocial.context.communication.domain;

import com.zima.zimasocial.context.communication.domain.entity.Recipient;
import com.zima.zimasocial.context.communication.domain.value.RecipientId;

import java.util.List;

public interface SubscriberSearch {
    List<Recipient> findSubscribers(RecipientId parent);
}
