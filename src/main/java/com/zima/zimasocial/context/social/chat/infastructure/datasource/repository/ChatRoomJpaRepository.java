package com.zima.zimasocial.context.social.chat.infastructure.datasource.repository;

import com.github.f4b6a3.uuid.UuidCreator;
import com.zima.zimasocial.context.social.author.value.AuthorId;
import com.zima.zimasocial.context.social.chat.entity.ChatRoom;
import com.zima.zimasocial.context.social.chat.entity.ChatRoomId;
import com.zima.zimasocial.context.social.chat.infastructure.datasource.dao.ChatRoomJpaDao;
import com.zima.zimasocial.context.social.chat.infastructure.datasource.entity.ChatRoomJpaEntity;
import com.zima.zimasocial.context.social.chat.repository.ChatRoomRepository;
import com.zima.zimasocial.entity.user.UserEntity;
import com.zima.zimasocial.repository.UserJpaRepository;
import com.zima.zimasocial.service.users.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class ChatRoomJpaRepository implements ChatRoomRepository {
    private final ChatRoomJpaDao chatRoomJpaDao;
    private final UserJpaRepository userJpaRepository;
    @Override
    public ChatRoomId nextId() {
        return new ChatRoomId(UuidCreator.getTimeOrdered());
    }

    @Override
    public Optional<ChatRoom> findById(ChatRoomId id) {
        return chatRoomJpaDao.findById(id.value()).map(ChatRoomJpaEntity::toDomain);
    }

    @Override
    public void save(ChatRoom chatRoom) {
        chatRoomJpaDao.save(new ChatRoomJpaEntity(chatRoom));
    }

    @Override
    public Optional<ChatRoom> findByParticipantsBetween(AuthorId participant1, AuthorId participant2) {
        return chatRoomJpaDao.findBetween(participant1.getValue(), participant2.getValue())
                .map(ChatRoomJpaEntity::toDomain);
    }

    @Override
    public Page<ChatRoom> findByParticipantIn(AuthorId recipientId, PageRequest pageRequest) {
        UserEntity user = userJpaRepository.findById(recipientId.getValue()).orElseThrow(UserNotFoundException::new);
        return chatRoomJpaDao
                .findByParticipant1IdOrParticipant2IdAndLastMessageIsNotNullOrderByLastMessageCreatedAtDesc(user.getId(), pageRequest).map(ChatRoomJpaEntity::toDomain);
    }

    @Override
    public void delete(ChatRoom chatRoom) {
        chatRoomJpaDao.deleteById(chatRoom.getId().value());
    }
}
