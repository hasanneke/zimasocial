package com.zima.zimasocial.context.social.chat.repository;

import com.zima.zimasocial.context.social.author.value.AuthorDomainId;
import com.zima.zimasocial.context.social.chat.entity.ChatRoom;
import com.zima.zimasocial.context.social.chat.entity.ChatRoomId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;
public interface ChatRoomRepository {
    ChatRoomId nextId();
    Optional<ChatRoom> findById(ChatRoomId id);
    void save(ChatRoom chatRoom);
    Optional<ChatRoom> findByParticipantsBetween(AuthorDomainId participant1, AuthorDomainId participant2);
    Page<ChatRoom> findByParticipantIn(AuthorDomainId recipientId, PageRequest pageRequest);
    void delete(ChatRoom chatRoom);
}
