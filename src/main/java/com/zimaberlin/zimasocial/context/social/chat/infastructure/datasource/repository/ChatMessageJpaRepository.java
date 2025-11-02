package com.zimaberlin.zimasocial.context.social.chat.infastructure.datasource.repository;

import com.github.f4b6a3.uuid.UuidCreator;
import com.zimaberlin.zimasocial.context.social.chat.entity.ChatMessage;
import com.zimaberlin.zimasocial.context.social.chat.entity.ChatMessageId;
import com.zimaberlin.zimasocial.context.social.chat.entity.ChatRoomId;
import com.zimaberlin.zimasocial.context.social.chat.infastructure.datasource.dao.ChatMessageJpaDAO;
import com.zimaberlin.zimasocial.context.social.chat.infastructure.datasource.entity.ChatMessageJpaEntity;
import com.zimaberlin.zimasocial.context.social.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatMessageJpaRepository implements ChatMessageRepository {
    private final ChatMessageJpaDAO chatMessageJpaDAO;
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
        return chatMessageJpaDAO.findByChatRoomIdOrderByCreatedAtDesc(chatRoomId.value(), pageRequest).map(ChatMessageJpaEntity::toDomain);
    }

    @Override
    public ChatMessage findById(ChatMessageId lastMessageId) {
        return null;
    }
}
