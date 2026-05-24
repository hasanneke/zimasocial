package com.zima.zimasocial.context.social.chat;

import com.zima.zimasocial.context.social.author.entity.AuthorDomain;
import com.zima.zimasocial.context.social.author.value.AuthorDomainId;
import com.zima.zimasocial.context.social.chat.entity.ChatMessage;
import com.zima.zimasocial.context.social.chat.entity.ChatMessageId;
import com.zima.zimasocial.context.social.chat.entity.ChatRoom;
import com.zima.zimasocial.context.social.chat.entity.ChatRoomId;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ChatTestUtility {
    public static ChatMessageId mockChatMessageId() {
        return new ChatMessageId(UUID.randomUUID());
    }
    public static ChatRoomId mockChatRoomId() {
        return new ChatRoomId(UUID.randomUUID());
    }
    public static AuthorDomain mockAuthor(Long authorId) {
        return new AuthorDomain(new AuthorDomainId(authorId), "mock", "mockName", LocalDateTime.now());
    }
    public static AuthorDomainId mockAuthorId() {
        return new AuthorDomainId(ThreadLocalRandom.current().nextLong());
    }
    public static AuthorDomain mockAuthor() {
        return new AuthorDomain(new AuthorDomainId(ThreadLocalRandom.current().nextLong()), "mock", "mockName", LocalDateTime.now());
    }
    public static ChatRoom mockChatRoom() {
        return new ChatRoom(new ChatRoomId(UUID.randomUUID()), mockAuthor(), mockAuthor());
    }
    public static ChatRoom mockChatRoom(Long participant1, Long participant2) {
        return new ChatRoom(new ChatRoomId(UUID.randomUUID()), mockAuthor(participant1), mockAuthor(participant2));
    }

    public static ChatRoom mockChatRoomPlain(AuthorDomainId participant1, AuthorDomainId participant2) {
        return new ChatRoom(new ChatRoomId(UUID.randomUUID()), mockAuthor(participant1.getValue()), mockAuthor(participant2.getValue()));
    }
    public static ChatRoom mockChatRoomPlain(AuthorDomain participant1, AuthorDomain participant2) {
        return new ChatRoom(new ChatRoomId(UUID.randomUUID()), participant1, participant2);
    }

    public static ChatMessage mockMessage(Long senderId, Long receiverId, String message) {
        return new ChatMessage(
                new ChatMessageId(UUID.randomUUID()),
                new ChatRoomId(UUID.randomUUID()),
                new AuthorDomainId(senderId),
                message,
                OffsetDateTime.now());
    }
}
