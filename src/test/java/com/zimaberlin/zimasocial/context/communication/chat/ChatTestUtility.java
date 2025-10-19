package com.zimaberlin.zimasocial.context.communication.chat;

import com.zimaberlin.zimasocial.context.communication.chat.entity.ChatMessage;
import com.zimaberlin.zimasocial.context.communication.chat.entity.ChatMessageId;
import com.zimaberlin.zimasocial.context.communication.chat.entity.ChatRoom;
import com.zimaberlin.zimasocial.context.communication.chat.entity.ChatRoomId;
import com.zimaberlin.zimasocial.context.communication.domain.Recipient;
import com.zimaberlin.zimasocial.context.communication.domain.RecipientId;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ChatTestUtility {
    public static ChatMessageId mockChatMessageId() {
        return new ChatMessageId(UUID.randomUUID());
    }
    public static ChatRoomId mockChatRoomId() {
        return new ChatRoomId(UUID.randomUUID());
    }
    public static Recipient mockRecipient(Long recipientId) {
        return new Recipient(new RecipientId(recipientId), "mock", Set.of(), "mock@example.com", "", "");
    }
    public static RecipientId mockRecipientId() {
        return new RecipientId(ThreadLocalRandom.current().nextLong());
    }
    public static Recipient mockRecipient() {
        return new Recipient(new RecipientId(ThreadLocalRandom.current().nextLong()), "mock", Set.of(), "mock@example.com", "", "");
    }
    public static ChatRoom mockChatRoom() {
        return new ChatRoom(new ChatRoomId(UUID.randomUUID()), mockRecipient(), mockRecipient());
    }
    public static ChatRoom mockChatRoom(Long participant1, Long participant2) {
        return new ChatRoom(new ChatRoomId(UUID.randomUUID()), mockRecipient(participant1), mockRecipient(participant2));
    }

    public static ChatRoom mockChatRoomPlain(RecipientId participant1, RecipientId participant2) {
        return new ChatRoom(new ChatRoomId(UUID.randomUUID()), mockRecipient(participant1.getValue()), mockRecipient(participant2.getValue()));
    }
    public static ChatRoom mockChatRoomPlain(Recipient participant1, Recipient participant2) {
        return new ChatRoom(new ChatRoomId(UUID.randomUUID()), participant1, participant2);
    }

    public static ChatMessage mockMessage(Long senderId, Long receiverId, String message) {
        return new ChatMessage(
                new ChatMessageId(UUID.randomUUID()),
                new ChatRoomId(UUID.randomUUID()),
                new RecipientId(senderId),
                message,
                LocalDateTime.now());
    }
}
