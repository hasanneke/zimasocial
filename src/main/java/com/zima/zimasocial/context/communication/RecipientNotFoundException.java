package com.zima.zimasocial.context.communication;

import com.zima.zimasocial.context.communication.domain.value.RecipientId;
import com.zima.zimasocial.shared.exception.DataNotFoundException;

public class RecipientNotFoundException extends DataNotFoundException {
    public RecipientNotFoundException(RecipientId recipientId) {
        super("recipient_not_found", "Recipient with id: %d not found".formatted(recipientId.getValue()));
    }

    public RecipientNotFoundException() {
        super("recipient_not_found", "Recipient found");
    }
}
