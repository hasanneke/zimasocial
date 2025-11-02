package com.zimaberlin.zimasocial.context.social.chat.exception;

import com.zimaberlin.zimasocial.exception.BadRequestException;

public class ChatMessageCannotBeEmptyException extends BadRequestException {
    public ChatMessageCannotBeEmptyException() {
        super("chat_message_cannot_be_empty", "Chat message cannot be empty");
    }
}
