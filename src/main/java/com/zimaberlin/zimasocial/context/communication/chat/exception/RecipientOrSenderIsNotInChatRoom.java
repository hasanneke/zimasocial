package com.zimaberlin.zimasocial.context.communication.chat.exception;

import com.zimaberlin.zimasocial.exception.BadRequestException;

public class RecipientOrSenderIsNotInChatRoom extends BadRequestException {
    public RecipientOrSenderIsNotInChatRoom() {
        super("recipient_or_sender_is_not_in_room", "Recipient is not in room");
    }
}
