package com.zimaberlin.zimasocial.context.communication.chat.entity;

import com.zimaberlin.zimasocial.context.communication.domain.RecipientId;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

public record ChatMessage(ChatMessageId id, ChatRoomId chatRoomId, RecipientId senderId, String message, LocalDateTime sentAt) {
    public ChatMessage {
        Assert.hasLength(message, "message cannot be empty");
        Assert.notNull(chatRoomId, "chatRoomId cannot be null");
        Assert.notNull(id, "id cannot be null");
        Assert.notNull(senderId, "senderId cannot be null");
        Assert.notNull(sentAt, "sentAt cannot be null");
    }
}