package com.zima.zimasocial.context.communication.controller.views;

import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.context.social.chat.entity.ChatRoom;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ChatRoomView {
    private final UUID id;
    private final RecipientView otherParticipant;
    private ChatMessageView lastMessage;

    public ChatRoomView(ChatRoom chatRoom, Author me) {
        this.id = chatRoom.getId().value();
        this.otherParticipant = new RecipientView(chatRoom.getOtherParticipant(me));
        if(chatRoom.getLastMessage() != null){
            this.lastMessage = new ChatMessageView(chatRoom.getLastMessage());
        }
    }

}
