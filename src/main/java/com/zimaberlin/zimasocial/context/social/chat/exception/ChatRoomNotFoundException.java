package com.zimaberlin.zimasocial.context.social.chat.exception;

import com.zimaberlin.zimasocial.exception.DataNotFoundException;

public class ChatRoomNotFoundException extends DataNotFoundException {
    public ChatRoomNotFoundException() {
        super("chat_room_not_found", "Chat room not found");
    }
}
