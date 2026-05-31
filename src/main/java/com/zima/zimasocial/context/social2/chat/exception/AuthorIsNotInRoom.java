package com.zima.zimasocial.context.social2.chat.exception;

import com.zima.zimasocial.exception.BadRequestException;

public class AuthorIsNotInRoom extends BadRequestException {
    public AuthorIsNotInRoom() {
        super("recipient_or_sender_is_not_in_room", "Recipient is not in room");
    }
}
