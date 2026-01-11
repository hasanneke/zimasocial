package com.zima.zimasocial.context.social.chat.infastructure.datasource.dao;

import com.zima.zimasocial.context.social.chat.infastructure.datasource.entity.ChatRoomJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public interface ChatRoomJpaDao extends JpaRepository<ChatRoomJpaEntity, UUID> {
    @Query("""
        SELECT chatRoom FROM ChatRoomJpaEntity chatRoom
        JOIN UserEntity participant ON participant.id = chatRoom.participant1Id
        JOIN UserEntity participant2 ON participant2.id = chatRoom.participant2Id
        WHERE participant.id = :participantId OR participant2.id = :participantId
        AND chatRoom.lastMessage IS NOT NULL
        ORDER BY chatRoom.lastMessage.createdAt DESC
       """)
    Page<ChatRoomJpaEntity> findByParticipant1IdOrParticipant2IdAndLastMessageIsNotNullOrderByLastMessageCreatedAtDesc(Long participantId, PageRequest pageRequest);
    @Query("""
            SELECT chatRoom FROM ChatRoomJpaEntity chatRoom WHERE
            (chatRoom.participant1Id = :participant1Id AND chatRoom.participant2Id = :participant2Id)
            OR
            (chatRoom.participant1Id = :participant2Id AND chatRoom.participant2Id = :participant1Id)
    """)
    Optional<ChatRoomJpaEntity> findBetween(@Param("participant1Id") Long participant1Id,
                                            @Param("participant2Id")  Long participant2Id);
}
