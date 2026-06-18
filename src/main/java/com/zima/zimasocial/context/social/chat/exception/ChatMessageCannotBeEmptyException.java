package com.zima.zimasocial.context.social.chat.exception;

import com.zima.zimasocial.shared.exception.BadRequestException;

public class ChatMessageCannotBeEmptyException extends BadRequestException {
    public ChatMessageCannotBeEmptyException() {
        super("chat_message_cannot_be_empty", "Chat message cannot be empty");
    }
}
