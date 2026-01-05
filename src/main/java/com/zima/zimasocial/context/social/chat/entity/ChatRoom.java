package com.zima.zimasocial.context.social.chat.entity;

import com.zima.zimasocial.context.social.author.entity.Author;
import com.zima.zimasocial.context.social.chat.event.ChatMessageSentEvent;
import com.zima.zimasocial.context.social.chat.exception.AuthorIsNotInRoom;
import com.zima.zimasocial.shared.StaticEventPublisher;
import lombok.Getter;
import org.springframework.util.Assert;

import java.time.OffsetDateTime;

@Getter
public class ChatRoom {
    private ChatRoomId id;
    private Author participant1;
    private Author participant2;
    private ChatMessage lastMessage;
    public ChatRoom(ChatRoomId id, Author participant1, Author participant2, ChatMessage lastMessage) {
        Assert.notNull(participant1, "participant1 cannot be null");
        Assert.notNull(participant2, "participant1 cannot be null");
        Assert.isTrue(id != null, "ChatRoomId cannot be null");
        this.id = id;
        this.participant1 = participant1;
        this.participant2 = participant2;
        this.lastMessage = lastMessage;
    }

    public ChatRoom(ChatRoomId id, Author participant1, Author participant2) {
        Assert.notNull(participant1, "participant1 cannot be null");
        Assert.notNull(participant2, "participant1 cannot be null");
        Assert.isTrue(id != null, "ChatRoomId cannot be null");
        this.id = id;
        this.participant1 = participant1;
        this.participant2 = participant2;
    }

    public ChatMessage sendMessage(String message, Author sender, Author receiver, ChatMessageId chatMessageId) {
        if(!(sender.equals(participant1) || sender.equals(participant2))){
            throw new AuthorIsNotInRoom();
        }
        ChatMessage chatMessage = new ChatMessage(chatMessageId, id, sender.getId(), message, OffsetDateTime.now());
        this.lastMessage = chatMessage;
        StaticEventPublisher.publishEvent(new ChatMessageSentEvent(sender, receiver, chatMessage));
        return chatMessage;
    }

    public Author getOtherParticipant(Author me) {
        if(me.equals(participant1)) {
            return participant2;
        }else if(me.equals(participant2)){
            return participant1;
        }else{
            throw new AuthorIsNotInRoom();
        }
    }

    public boolean isAuthorInRoom(Author author){
        return author.equals(participant1) || author.equals(participant2);
    }
}
