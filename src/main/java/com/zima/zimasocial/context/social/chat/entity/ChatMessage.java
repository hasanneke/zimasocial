package com.zima.zimasocial.context.social.chat.entity;

import com.zima.zimasocial.context.social.author.value.AuthorDomainId;
import org.springframework.util.Assert;

import java.time.OffsetDateTime;

public record ChatMessage(ChatMessageId id, ChatRoomId chatRoomId, AuthorDomainId senderId, String message, OffsetDateTime sentAt) {
    public ChatMessage {
        Assert.hasLength(message, "message cannot be empty");
        Assert.notNull(chatRoomId, "chatRoomId cannot be null");
        Assert.notNull(id, "id cannot be null");
        Assert.notNull(senderId, "senderId cannot be null");
        Assert.notNull(sentAt, "sentAt cannot be null");
    }
}