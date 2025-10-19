package com.zimaberlin.zimasocial.context.communication.chat.exception;

import com.zimaberlin.zimasocial.exception.BadRequestException;

public class ChatRoomAlreadyExist extends BadRequestException {
    public ChatRoomAlreadyExist() {
        super("chat_room_already_exist", "Chat room already exist");
    }
}
