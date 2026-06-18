package com.zima.zimasocial.context.social.chat.exception;

import com.zima.zimasocial.shared.exception.BadRequestException;

public class AuthorIsNotInRoom extends BadRequestException {
    public AuthorIsNotInRoom() {
        super("recipient_or_sender_is_not_in_room", "Recipient is not in room");
    }
}
