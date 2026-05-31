package com.zima.zimasocial.context.social.chat.exception;

import com.zima.zimasocial.exception.DataNotFoundException;

public class ChatRoomNotFoundException extends DataNotFoundException {
    public ChatRoomNotFoundException() {
        super("chat_room_not_found", "Chat room not found");
    }
}
