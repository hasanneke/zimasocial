package com.zimaberlin.zimasocial.context.communication.controller.views;

import com.zimaberlin.zimasocial.context.communication.chat.entity.ChatMessage;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class ChatMessageView {
    private final UUID id;
    private final String message;
    private final Long senderId;
    private final LocalDateTime sentAt;

    public ChatMessageView(ChatMessage chatMessage) {
        this.id = chatMessage.id().value();
        this.message = chatMessage.message();
        this.senderId = chatMessage.senderId().getValue();
        this.sentAt = chatMessage.sentAt();
    }
}
