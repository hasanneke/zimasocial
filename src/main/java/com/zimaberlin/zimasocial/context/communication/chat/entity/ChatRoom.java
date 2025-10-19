package com.zimaberlin.zimasocial.context.communication.chat.entity;

import com.zimaberlin.zimasocial.context.communication.chat.event.ChatMessageSentEvent;
import com.zimaberlin.zimasocial.context.communication.chat.exception.RecipientOrSenderIsNotInChatRoom;
import com.zimaberlin.zimasocial.context.communication.domain.Recipient;
import com.zimaberlin.zimasocial.shared.StaticEventPublisher;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

public record ChatRoom(ChatRoomId id, Recipient participant1, Recipient participant2) {
    public ChatRoom {
        Assert.notNull(participant1, "participant1 cannot be null");
        Assert.notNull(participant2, "participant1 cannot be null");
        Assert.isTrue(id != null, "ChatRoomId cannot be null");
    }

    public ChatMessage sendMessage(String message, Recipient sender, Recipient receiver, ChatMessageId chatMessageId) {
        if(!(sender.equals(participant1) || sender.equals(participant2))){
            throw new RecipientOrSenderIsNotInChatRoom();
        }
        ChatMessage chatMessage = new ChatMessage(chatMessageId, id, sender.getRecipientId(), message, LocalDateTime.now());
        StaticEventPublisher.publishEvent(new ChatMessageSentEvent(sender, receiver, chatMessage));
        return chatMessage;
    }

    public Recipient getOtherParticipant(Recipient me) {
        if(me.equals(participant1)) {
            return participant2;
        }else if(me.equals(participant2)){
            return participant1;
        }else{
            throw new RecipientOrSenderIsNotInChatRoom();
        }
    }
}
