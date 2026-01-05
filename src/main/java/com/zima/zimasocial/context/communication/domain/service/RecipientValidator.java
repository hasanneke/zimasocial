package com.zima.zimasocial.context.communication.domain.service;

import com.zima.zimasocial.context.communication.domain.value.RecipientId;

public interface RecipientValidator {
    boolean canBePushed(RecipientId recipientId);
}
