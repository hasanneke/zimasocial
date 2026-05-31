package com.zima.zimasocial.context.social2.chat.exception;

import com.zima.zimasocial.exception.BadRequestException;

public class ChatRoomAlreadyExist extends BadRequestException {
    public ChatRoomAlreadyExist() {
        super("chat_room_already_exist", "Chat room already exist");
    }
}
