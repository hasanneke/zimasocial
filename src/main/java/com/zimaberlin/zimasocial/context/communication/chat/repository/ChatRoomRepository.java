package com.zimaberlin.zimasocial.context.communication.chat.repository;

import com.zimaberlin.zimasocial.context.communication.chat.entity.ChatRoom;
import com.zimaberlin.zimasocial.context.communication.chat.entity.ChatRoomId;
import com.zimaberlin.zimasocial.context.communication.domain.RecipientId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;
public interface ChatRoomRepository {
    ChatRoomId nextId();
    Optional<ChatRoom> findById(ChatRoomId id);
    void save(ChatRoom chatRoom);
    Optional<ChatRoom> findByParticipantsBetween(RecipientId participant1, RecipientId participant2);
    Page<ChatRoom> findByParticipantIn(RecipientId recipientId, PageRequest pageRequest);
}
