package com.zimaberlin.zimasocial.context.communication.chat.infastructure.datasource.converters;

import com.zimaberlin.zimasocial.context.communication.chat.entity.ChatRoom;
import com.zimaberlin.zimasocial.context.communication.chat.entity.ChatRoomId;
import com.zimaberlin.zimasocial.context.communication.chat.infastructure.datasource.entity.ChatRoomJpaEntity;
import com.zimaberlin.zimasocial.context.communication.infastructure.UserEntityRecipientConverter;
import org.springframework.stereotype.Component;

@Component
public class ChatRoomJpaEntityChatRoomConverter {
    public static ChatRoom convertToChatRoom(ChatRoomJpaEntity chatRoomJpaEntity) {
        return new ChatRoom(new ChatRoomId(chatRoomJpaEntity.getId()),
                UserEntityRecipientConverter.toRecipient(chatRoomJpaEntity.getParticipant1()),
                UserEntityRecipientConverter.toRecipient(chatRoomJpaEntity.getParticipant2()));
    }
}
