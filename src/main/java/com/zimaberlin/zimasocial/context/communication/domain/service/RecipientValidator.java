package com.zimaberlin.zimasocial.context.communication.domain.service;

import com.zimaberlin.zimasocial.context.communication.domain.value.RecipientId;

public interface RecipientValidator {
    boolean canBePushed(RecipientId recipientId);
}
