package com.zima.zimasocial.context.communication.controller.views;

import com.zima.zimasocial.context.social.chat.entity.ChatMessage;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
public class ChatMessageView {
    private final UUID id;
    private final String message;
    private final Long senderId;
    private final OffsetDateTime sentAt;

    public ChatMessageView(ChatMessage chatMessage) {
        this.id = chatMessage.getId().getValue();
        this.message = chatMessage.getContent();
        this.senderId = chatMessage.getSenderId().getValue();
        this.sentAt = chatMessage.getSentAt();
    }
}
