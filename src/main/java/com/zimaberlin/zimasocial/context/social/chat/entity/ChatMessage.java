package com.zimaberlin.zimasocial.context.social.chat.entity;

import com.zimaberlin.zimasocial.context.social.author.AuthorId;
import org.springframework.util.Assert;

import java.time.OffsetDateTime;

public record ChatMessage(ChatMessageId id, ChatRoomId chatRoomId, AuthorId senderId, String message, OffsetDateTime sentAt) {
    public ChatMessage {
        Assert.hasLength(message, "message cannot be empty");
        Assert.notNull(chatRoomId, "chatRoomId cannot be null");
        Assert.notNull(id, "id cannot be null");
        Assert.notNull(senderId, "senderId cannot be null");
        Assert.notNull(sentAt, "sentAt cannot be null");
    }
}