package com.zimaberlin.zimasocial.context.communication.controller.views;

import com.zimaberlin.zimasocial.context.communication.chat.entity.ChatRoom;
import com.zimaberlin.zimasocial.context.communication.domain.Recipient;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ChatRoomView {
    private final UUID id;
    private final RecipientView otherParticipant;

    public ChatRoomView(ChatRoom chatRoom, Recipient me) {
        this.id = chatRoom.id().value();
        this.otherParticipant = new RecipientView(chatRoom.getOtherParticipant(me));
    }
}
