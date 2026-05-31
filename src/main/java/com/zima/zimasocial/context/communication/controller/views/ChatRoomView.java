package com.zima.zimasocial.context.communication.controller.views;

import com.zima.zimasocial.context.social2.chat.entity.ChatRoom;
import com.zima.zimasocial.context.social2.domain.entity.Author;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ChatRoomView {
    private final UUID id;
    private final RecipientView otherParticipant;
    private ChatMessageView lastMessage;

    public ChatRoomView(ChatRoom chatRoom, Author me) {
        this.id = chatRoom.getId().getValue();
        this.otherParticipant = new RecipientView(chatRoom.getOtherParticipant(me));
        if(chatRoom.getLastMessage() != null){
            this.lastMessage = new ChatMessageView(chatRoom.getLastMessage());
        }
    }

}
