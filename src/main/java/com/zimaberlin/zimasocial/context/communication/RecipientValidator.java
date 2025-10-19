package com.zimaberlin.zimasocial.context.communication;

import com.zimaberlin.zimasocial.context.communication.domain.RecipientId;

public interface RecipientValidator {
    boolean canBePushed(RecipientId recipientId);
}
