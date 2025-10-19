package com.zimaberlin.zimasocial.context.communication.chat.infastructure.datasource.repository;

import com.github.f4b6a3.uuid.UuidCreator;
import com.zimaberlin.zimasocial.context.communication.chat.entity.ChatMessage;
import com.zimaberlin.zimasocial.context.communication.chat.entity.ChatMessageId;
import com.zimaberlin.zimasocial.context.communication.chat.entity.ChatRoomId;
import com.zimaberlin.zimasocial.context.communication.chat.infastructure.datasource.converters.ChatMessageJpaEntityChatMessageConverter;
import com.zimaberlin.zimasocial.context.communication.chat.infastructure.datasource.dao.ChatMessageJpaDAO;
import com.zimaberlin.zimasocial.context.communication.chat.infastructure.datasource.entity.ChatMessageJpaEntity;
import com.zimaberlin.zimasocial.context.communication.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatMessageJpaRepository implements ChatMessageRepository {
    private final ChatMessageJpaDAO chatMessageJpaDAO;
    private final ChatMessageJpaEntityChatMessageConverter chatMessageJpaEntityChatMessageConverter;
    @Override
    public ChatMessageId nextId() {
        return new ChatMessageId(UuidCreator.getTimeOrdered());
    }

    @Override
    public void save(ChatMessage chatMessage) {
        chatMessageJpaDAO.save(new ChatMessageJpaEntity(chatMessage));
    }

    @Override
    public Page<ChatMessage> findAllByChatRoomIdOrderBySentAtDesc(ChatRoomId chatRoomId, PageRequest pageRequest) {
        return chatMessageJpaDAO.findByChatRoomIdOrderByCreatedAtDesc(chatRoomId.value(), pageRequest).map(chatMessageJpaEntityChatMessageConverter::toChatMessage);
    }
}
