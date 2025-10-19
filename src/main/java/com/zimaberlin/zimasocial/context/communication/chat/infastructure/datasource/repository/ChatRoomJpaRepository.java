package com.zimaberlin.zimasocial.context.communication.chat.infastructure.datasource.repository;

import com.github.f4b6a3.uuid.UuidCreator;
import com.zimaberlin.zimasocial.context.communication.chat.entity.ChatRoomId;
import com.zimaberlin.zimasocial.context.communication.chat.entity.ChatRoom;
import com.zimaberlin.zimasocial.context.communication.chat.infastructure.datasource.converters.ChatRoomJpaEntityChatRoomConverter;
import com.zimaberlin.zimasocial.context.communication.chat.infastructure.datasource.dao.ChatRoomJpaDao;
import com.zimaberlin.zimasocial.context.communication.chat.infastructure.datasource.entity.ChatRoomJpaEntity;
import com.zimaberlin.zimasocial.context.communication.chat.repository.ChatRoomRepository;
import com.zimaberlin.zimasocial.context.communication.domain.RecipientId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class ChatRoomJpaRepository implements ChatRoomRepository {
    private final ChatRoomJpaDao chatRoomJpaDao;
    @Override
    public ChatRoomId nextId() {
        return new ChatRoomId(UuidCreator.getTimeOrdered());
    }

    @Override
    public Optional<ChatRoom> findById(ChatRoomId id) {
        return chatRoomJpaDao.findById(id.value()).map(ChatRoomJpaEntityChatRoomConverter::convertToChatRoom);
    }

    @Override
    public void save(ChatRoom chatRoom) {
        chatRoomJpaDao.save(new ChatRoomJpaEntity(chatRoom));
    }

    @Override
    public Optional<ChatRoom> findByParticipantsBetween(RecipientId participant1, RecipientId participant2) {
        return chatRoomJpaDao.findBetween(participant1.getValue(), participant2.getValue())
                .map(ChatRoomJpaEntityChatRoomConverter::convertToChatRoom);
    }

    @Override
    public Page<ChatRoom> findByParticipantIn(RecipientId recipientId, PageRequest pageRequest) {
        return chatRoomJpaDao.findByParticipant1OrParticipant2(recipientId.getValue(), recipientId.getValue(), pageRequest).map(ChatRoomJpaEntityChatRoomConverter::convertToChatRoom);
    }
}
