package com.zimaberlin.zimasocial.context.communication.chat.infastructure.datasource.converters;

import com.zimaberlin.zimasocial.context.communication.chat.entity.ChatMessage;
import com.zimaberlin.zimasocial.context.communication.chat.entity.ChatMessageId;
import com.zimaberlin.zimasocial.context.communication.chat.entity.ChatRoomId;
import com.zimaberlin.zimasocial.context.communication.chat.infastructure.datasource.entity.ChatMessageJpaEntity;
import com.zimaberlin.zimasocial.context.communication.domain.RecipientId;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageJpaEntityChatMessageConverter {
    public ChatMessage toChatMessage(ChatMessageJpaEntity chatMessageJpaEntity) {
        return new ChatMessage(
                new ChatMessageId(chatMessageJpaEntity.getId()),
                new ChatRoomId(chatMessageJpaEntity.getChatRoomId()),
                new RecipientId(chatMessageJpaEntity.getSenderId()),
                chatMessageJpaEntity.getContent(),
                chatMessageJpaEntity.getCreatedAt()
        );
    }
}
