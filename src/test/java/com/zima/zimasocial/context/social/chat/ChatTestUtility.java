package com.zima.zimasocial.context.social.chat;

import com.zima.zimasocial.AuthorFixture;
import com.zima.zimasocial.context.social2.chat.entity.ChatRoom;
import com.zima.zimasocial.context.social2.chat.value.ChatMessageId;
import com.zima.zimasocial.context.social2.chat.value.ChatRoomId;
import com.zima.zimasocial.context.social2.domain.entity.Author;
import com.zima.zimasocial.context.social2.domain.value.AuthorId;

import java.util.UUID;

public class ChatTestUtility {
    public static ChatMessageId mockChatMessageId() {
        return new ChatMessageId(UUID.randomUUID());
    }
    public static ChatRoomId mockChatRoomId() {
        return new ChatRoomId(UUID.randomUUID());
    }

    public static ChatRoom mockChatRoom(Long participant1, Long participant2) {
        return new ChatRoom(new ChatRoomId(UUID.randomUUID()), AuthorFixture.validAuthor(new AuthorId(participant1)).getId(), AuthorFixture.validAuthor(new AuthorId(participant2)).getId());
    }

    public static ChatRoom mockChatRoom(Author participant1, Author participant2) {
        ChatRoom room = new ChatRoom(new ChatRoomId(UUID.randomUUID()), participant1.getId(), participant2.getId());
        room.setParticipant1(participant1);
        room.setParticipant2(participant2);
        return room;
    }
}
