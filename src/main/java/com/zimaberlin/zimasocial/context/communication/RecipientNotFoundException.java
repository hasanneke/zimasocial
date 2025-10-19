package com.zimaberlin.zimasocial.context.communication;

import com.zimaberlin.zimasocial.context.communication.domain.RecipientId;
import com.zimaberlin.zimasocial.exception.DataNotFoundException;

public class RecipientNotFoundException extends DataNotFoundException {
    public RecipientNotFoundException(RecipientId recipientId) {
        super("recipient_not_found", "Recipient with id: %d not found".formatted(recipientId.getValue()));
    }

    public RecipientNotFoundException() {
        super("recipient_not_found", "Recipient found");
    }
}
