package com.zima.zimasocial.context.social2.chat.repository;

import com.github.f4b6a3.uuid.UuidCreator;
import com.zima.zimasocial.context.social2.chat.value.ChatRoomId;
import com.zima.zimasocial.context.social2.chat.entity.ChatRoom;
import com.zima.zimasocial.context.social2.domain.value.AuthorId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public interface ChatRoomRepository extends JpaRepository<ChatRoom, ChatRoomId> {
    @Query("""
        SELECT chatRoom FROM ChatRoom chatRoom
        JOIN Author participant ON participant.id = chatRoom.participant1Id
        JOIN Author participant2 ON participant2.id = chatRoom.participant2Id
        WHERE participant.id = :participantId OR participant2.id = :participantId
        AND chatRoom.lastMessage IS NOT NULL
        ORDER BY chatRoom.lastMessage.sentAt DESC
       """)
    Page<ChatRoom> findChatRooms(AuthorId participantId, Pageable pageable);
    @Query("""
            SELECT chatRoom FROM ChatRoom chatRoom WHERE
            (chatRoom.participant1Id = :participant1Id AND chatRoom.participant2Id = :participant2Id)
            OR
            (chatRoom.participant1Id = :participant2Id AND chatRoom.participant2Id = :participant1Id)
    """)
    Optional<ChatRoom> findBetween(@Param("participant1Id") AuthorId participant1Id,
                                   @Param("participant2Id") AuthorId participant2Id);

    default UUID nextId() {
        return UuidCreator.getTimeOrdered();
    }
}
