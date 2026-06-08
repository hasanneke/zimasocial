package com.zima.zimasocial.context.communication.domain;

import com.zima.zimasocial.context.communication.domain.entity.RecipientDomain;
import com.zima.zimasocial.context.communication.domain.value.RecipientId;

import java.util.List;

public interface SubscriberSearch {
    List<RecipientDomain> findSubscribers(RecipientId parent);
}
